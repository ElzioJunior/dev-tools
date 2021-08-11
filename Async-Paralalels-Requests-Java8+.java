public class ParallelCallsDemoService {

    @Inject
    RestApiClient restApiClient;

    public List<ToDo> getToDos(List<String> ids){
        List<CompletableFuture<ToDo>> futures = ids.stream().map(id -> getToDoAsync(id)).collect(Collectors.toList());
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }


    CompletableFuture<ToDo> getToDoAsync(String id){
        return CompletableFuture.supplyAsync(new Supplier<ToDo>() {
            @Override
            public ToDo get() {
                return restApiClient.getToDo(id);
            }
        });
    }

}