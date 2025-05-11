package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.Invoice;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceCommandMapper {
    InvoiceCommandDto toDto(Invoice invoice);
}
