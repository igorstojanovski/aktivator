package io.aktivator.events;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentSubmittedEventRepository extends CrudRepository<PaymentSubmittedEvent, Long> {

    List<PaymentSubmittedEvent> findByDonationId(Long donationId);
}
