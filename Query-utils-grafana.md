### PROMETHEUS QUERY SUM INCREASING
> sum(increase(METRIC_NAME_HERE{server="FILTER_FOR_SERVER_NAME"}[$__range]))

where $__range gets the actual grafana filter range of dates
