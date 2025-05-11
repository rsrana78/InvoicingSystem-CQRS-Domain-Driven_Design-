package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.Invoice;
import com.xcelerate.invoicing.dto.query.response.InvoiceQueryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceQueryMapper {
    InvoiceQueryDto toDto(Invoice invoice);

    List<InvoiceQueryDto> toDto(List<Invoice> invoices);
}
