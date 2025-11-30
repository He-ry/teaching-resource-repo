package com.teach.controller;

import com.teach.domain.pojo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "状态接口")
@RestController
@RequestMapping("/api/v1/ping")
@Validated
public class SystemController {

    @Operation(summary = "ping 状态接口")
    @GetMapping("")
    public Result<HashMap<String, String>> ping() {
        HashMap<String, String> map = new HashMap<>();
        map.put("server", "reaching-resource-repo");
        map.put("status", "running");
        return Result.success(map);
    }
}
