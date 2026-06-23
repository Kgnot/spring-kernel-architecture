package org.example.microkernelspring.core.sale.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.event.CustomerCreatedEvent;
import org.example.microkernelspring.core.sale.persistence.entity.Customer;
import org.example.microkernelspring.core.sale.persistence.entity.LkpCustomerType;
import org.example.microkernelspring.core.sale.persistence.repository.CustomerRepository;
import org.example.microkernelspring.core.sale.usecase.command.CreateCustomerCommand;
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