package com.github.iamhi.hiintegration.wildserver.core;

import reactor.core.publisher.Flux;

public interface MessageSupplierService {

    Flux<String> getMessages(String token);
}
