    public Mono<ReissuedCardDTO> reissuedCard(Integer cardId, ReissueCardRequest reissueCardRequest) {
        return getReissueCardData(cardId, reissueCardRequest.getTaxId())
                .flatMap(isCurrentCardReissueValid(reissueCardRequest))
                .filter(isCurrentCardNotCanceled())
                .switchIfEmpty(Mono.error(new CardAlreadyCanceledException()))
                .flatMap(reissueCurrentCardNotCanceled(reissueCardRequest))
                .onErrorResume(CardAlreadyCanceledException.class, e -> reissueCard(cardId));
    }

        private Function<ReissueDTO, Mono<ReissueDTO>> isCurrentCardReissueValid(ReissueCardRequest reissueCardRequest) {
        return reissueDTO -> Mono.just(reissueDTO)
                .doOnSuccess(dto -> log.info("Iniciando validação de solicitação de segunda via {}", dto))
                .filter(isRequestNewCardStatusAllowedToReissue(reissueCardRequest))
                .switchIfEmpty(Mono.error(new BusinessException("error.updateStatusNotAllowedToReissueCard")))
                .filter(isAccountStatusAllowedToCardReissue())
                .switchIfEmpty(Mono.error(new BusinessException("error.accountStatusNotAllowedToReissueCard")))
                .filter(isCardsFromAccountStageValid())
                .switchIfEmpty(Mono.error(new BusinessException("error.accountCardStageNotAllowed")))
                .filter(isCurrentCardNotVirtual())
                .switchIfEmpty(Mono.error(new BusinessException("error.cardVirtualInvalidToReissue")))
                .doOnSuccess(dto -> log.info("Finalizado validação de segunda via"));
    }

    private Predicate<ReissueDTO> isCurrentCardNotCanceled() {
        return reissueDTO -> {
            List<CardStatusDTO> canceledCardStatus = reissueDTO.getCardStatusCancelList();
            CardDTO currentCard = reissueDTO.getCurrentCard();

            return canceledCardStatus.stream()
                    .noneMatch(cardStatusDTO -> cardStatusDTO.getCardStatus().getId().equals(currentCard.getStatusId()));
        };
    }