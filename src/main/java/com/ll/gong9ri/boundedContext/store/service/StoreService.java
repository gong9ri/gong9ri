package com.ll.gong9ri.boundedContext.store.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
	private final StoreRepository storeRepository;

	@Transactional(readOnly = true)
	public Optional<Store> findById(final Long id) {
		return storeRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Store> findByName(final String name) {
		return storeRepository.findByName(name);
	}

	@Transactional(readOnly = true)
	public RsData<Store> getStoreByName(final String storeName) {
		return findByName(storeName).map(RsData::successOf)
			.orElseGet(() -> RsData.of("F-1", "해당하는 스토어가 없습니다.", null));
	}
}
