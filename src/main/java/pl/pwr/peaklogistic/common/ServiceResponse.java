package pl.pwr.peaklogistic.common;

public record ServiceResponse<T>(OperationStatus operationStatus, T body) {

    public static <T> ServiceResponse<T> ok(T body){
        return new ServiceResponse<T>(OperationStatus.Ok, body);
    }

    public static <T> ServiceResponse<T> created(T body){
        return new ServiceResponse<>(OperationStatus.Created, body);
    }

    public static <T> ServiceResponse<T> noContent(){
        return new ServiceResponse<>(OperationStatus.NoContent, null);
    }

    public static <T> ServiceResponse<T> badRequest(){
        return new ServiceResponse<>(OperationStatus.BadRequest, null);
    }

    public static <T> ServiceResponse<T> unauthorized(){
        return new ServiceResponse<>(OperationStatus.Unauthorized, null);
    }

    public static <T> ServiceResponse<T> notFound(){
//        return new ServiceResponse<>(OperationStatus.NotFound, new Optional<T>.empty());
        return new ServiceResponse<>(OperationStatus.NotFound, null);
    }


}
