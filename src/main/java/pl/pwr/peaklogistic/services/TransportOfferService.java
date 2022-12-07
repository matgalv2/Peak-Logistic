package pl.pwr.peaklogistic.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import pl.pwr.peaklogistic.common.ServiceResponse;
import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;
import pl.pwr.peaklogistic.model.TransportOffer;
import pl.pwr.peaklogistic.model.TransportOrder;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.TransportOfferRepository;
import pl.pwr.peaklogistic.repository.TransportOrderRepository;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class TransportOfferService {

    private final TransportOfferRepository transportOfferRepository;
    private final TransportOrderRepository transportOrderRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ServiceResponse<List<TransportOffer>> getAllTransportOffers() {
        return ServiceResponse.ok(transportOfferRepository.findAll());
    }

    public ServiceResponse<TransportOffer> getTransportOfferById(long id) {
        return transportOfferRepository.findById(id).map(ServiceResponse::ok).orElse(ServiceResponse.notFound());
    }

//    public ServiceResponse<List<TransportOffer>> getTransportOffersByTransportOrderId(long id) {
//        return ServiceResponse.ok(transportOfferRepository.getTransportOffersByTransportOrderTransportOrderID(id));
//    }

    public ServiceResponse<List<TransportOffer>> getTransportOffersByCarrierId(long id) {
        return ServiceResponse.ok(transportOfferRepository.getTransportOffersByCarrierUserID(id));
    }


    public ServiceResponse<TransportOffer> createTransportOffer(PostTransportOffer postTransportOffer, long carrierID) {
        if (!userRepository.existsById(carrierID)
                || !transportOrderRepository.existsById(postTransportOffer.getTransportOrderID())) {
            return ServiceResponse.badRequest();
        } else {
            User carrier = userRepository.findById(carrierID).get();
//            TransportOrder order = transportOrderRepository.findById(postTransportOffer.getTransportOrderID()).get();
            return ServiceResponse.created(transportOfferRepository.save(toDomain(carrier, postTransportOffer.getTransportOrderID()).map(postTransportOffer)));
        }
    }

    public ServiceResponse<TransportOffer> deleteTransportOffer(long id) {
        if (!transportOfferRepository.existsById(id))
            return ServiceResponse.notFound();
        else {
            transportOfferRepository.deleteById(id);
            return ServiceResponse.noContent();
        }
    }

    private TypeMap<PostTransportOffer, TransportOffer> toDomain(User user, long orderID) {
        return mapper
                .typeMap(PostTransportOffer.class, TransportOffer.class)
                .addMapping(src -> user, TransportOffer::setCarrier)
                .addMapping(src -> orderID, TransportOffer::setTransportOrderID);
//                .addMapping(src -> transportOrder, TransportOffer::setTransportOrder);
    }
}
