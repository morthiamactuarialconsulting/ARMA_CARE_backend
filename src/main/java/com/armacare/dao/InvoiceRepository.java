package com.armacare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.armacare.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
