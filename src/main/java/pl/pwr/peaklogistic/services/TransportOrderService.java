package pl.pwr.peaklogistic.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.dto.request.jobOffer.PostJobOffer;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.model.JobOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TransportOrderService {
    private final TransportOrderRepository transportOrderRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    public ServiceResponse<List<TransportOrder>> getAllTransportOrders()
    {
        return ServiceResponse.ok(transportOrderRepository.findAll());
    }

    public ServiceResponse<TransportOrder> getTransportOrderById(long id){
        return transportOrderRepository.findById(id).map(ServiceResponse::ok).orElse(ServiceResponse.notFound());
    }

    public ServiceResponse<List<TransportOrder>> getTransportOrdersByCustomerId(long id){
        return ServiceResponse.ok(transportOrderRepository.getTransportOrdersByCustomerUserID(id));
    }


    public ServiceResponse<TransportOrder> createTransportOrder(PostTransportOrder postTransportOrder){
        return userRepository
                .findById(postTransportOrder.getCustomerID())
                .map(x -> ServiceResponse.created(transportOrderRepository.save(mapperWithUser(x).map(postTransportOrder))))
                .orElse(ServiceResponse.badRequest());

    }

    public ServiceResponse<TransportOrder> deleteTransportOrder(long id){
        if (!transportOrderRepository.existsById(id))
            return ServiceResponse.notFound();
        else{
            transportOrderRepository.deleteById(id);
            return ServiceResponse.noContent();
        }
    }

    private TypeMap<PostTransportOrder, TransportOrder> mapperWithUser(User user){
        return mapper
//                .addMappings(mapper -> mapper.map(src -> user, JobOffer::setCarrier));
                .typeMap(PostTransportOrder.class, TransportOrder.class)
                .addMapping(src -> user, TransportOrder::setCustomer);
    }
}
