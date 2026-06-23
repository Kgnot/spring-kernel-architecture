package org.example.microkernelspring.core.sale.application.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.domain.event.CustomerCreatedEvent;
import org.example.microkernelspring.core.sale.domain.entity.Customer;
import org.example.microkernelspring.core.sale.domain.entity.LkpCustomerType;
import org.example.microkernelspring.core.sale.application.repository.CustomerRepository;
import org.example.microkernelspring.core.sale.application.usecase.command.CreateCustomerCommand;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final EventBus eventBus; // Dependencia colocada, tú la gestionas

    @Transactional
    public void execute(CreateCustomerCommand command) {
        Customer customer = new Customer();
        customer.setTenantId(command.tenantId());
        customer.setProfileId(command.profileId());

        LkpCustomerType type = new LkpCustomerType();
        type.setId(command.customerTypeId());
        customer.setCustomerType(type);

        customer.setCompanyName(command.companyName());
        customer.setTaxId(command.taxId());
        customer.setCreditLimit(command.creditLimit());

        Customer savedCustomer = customerRepository.save(customer);

        eventBus.publish(new CustomerCreatedEvent(
                savedCustomer.getId(),
                savedCustomer.getTenantId(),
                savedCustomer.getTaxId()
        ));
    }
}