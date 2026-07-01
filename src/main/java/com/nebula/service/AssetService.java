package com.nebula.service;

import com.nebula.domain.Asset;
import com.nebula.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final RBloomFilter<String> assetBloomFilter;

    @Cacheable(value = "asset", key = "#id")
    public Asset getById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("资产不存在: " + id));
    }

    @Transactional
    public Asset create(Asset asset) {
        Asset saved = assetRepository.save(asset);
        // 写入布隆过滤器
        assetBloomFilter.add("asset:" + saved.getId());
        return saved;
    }

    @Transactional
    @CacheEvict(value = "asset", key = "#id")
    public Asset update(Long id, Asset asset) {
        Asset existing = getById(id);
        existing.setName(asset.getName());
        existing.setDescription(asset.getDescription());
        existing.setAssetType(asset.getAssetType());
        existing.setStatus(asset.getStatus());
        existing.setOwner(asset.getOwner());
        existing.setSensitive(asset.getSensitive());
        return assetRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "asset", key = "#id")
    public void delete(Long id) {
        assetRepository.deleteById(id);
    }

    public List<Asset> listAll() {
        return assetRepository.findAll();
    }
}
