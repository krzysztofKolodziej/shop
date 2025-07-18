package com.Shop.shop.exception;

import org.springframework.http.HttpStatusCode;

public record ErrorResponse(HttpStatusCode status, String message) {
}