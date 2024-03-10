package com.github.barjb.todo.Exceptions;

import java.util.List;
import lombok.Builder;

@Builder
public record CustomError(String reason, List<String> messages) {}
