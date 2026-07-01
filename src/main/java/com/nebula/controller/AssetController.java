package com.nebula.controller;

import com.nebula.annotation.RecordAudit;
import com.nebula.domain.Asset;
import com.nebula.domain.RestBean;
import com.nebula.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/{id}")
    public RestBean<Asset> getById(@PathVariable Long id) {
        return RestBean.success(assetService.getById(id));
    }

    @GetMapping
    public RestBean<List<Asset>> listAll() {
        return RestBean.success(assetService.listAll());
    }

    @PostMapping
    @RecordAudit("创建资产")
    public RestBean<Asset> create(@RequestBody Asset asset) {
        return RestBean.success(assetService.create(asset));
    }

    @PutMapping("/{id}")
    @RecordAudit("更新资产")
    public RestBean<Asset> update(@PathVariable Long id, @RequestBody Asset asset) {
        return RestBean.success(assetService.update(id, asset));
    }

    @DeleteMapping("/{id}")
    @RecordAudit("删除资产")
    public RestBean<Void> delete(@PathVariable Long id) {
        assetService.delete(id);
        return RestBean.success(null);
    }
}
