package com.ll.gong9ri.boundedContext.store.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
	private static final String NO_STORE_MESSAGE = "해당하는 스토어가 없습니다.";
	private final StoreRepository storeRepository;

	@Transactional(readOnly = true)
	public Optional<Store> findById(final Long id) {
		return storeRepository.findById(id);
	}

	@Cacheable("store")
	@Transactional(readOnly = true)
	public Optional<Store> findByMemberId(final Long memberId) {
		return storeRepository.findByMemberId(memberId);
	}

	@Transactional(readOnly = true)
	public Optional<Store> findByName(final String name) {
		return storeRepository.findByName(name);
	}

	@Transactional(readOnly = true)
	public List<Store> searchByName(final String name) {
		if (name.isBlank()) {
			return storeRepository.findAll();
		}

		return storeRepository.findDistinctByNameLike(name);
	}

	@Transactional(readOnly = true)
	public RsData<Store> getStoreByName(final String storeName) {
		return findByName(storeName).map(RsData::successOf)
			.orElseGet(() -> RsData.of("F-1", NO_STORE_MESSAGE, null));
	}

	public RsData<Store> create(final Member member, final String storeName) {
		Store store = Store.builder()
				.member(member)
				.name(storeName)
				.build();

		storeRepository.save(store);

		return RsData.successOf(store);
	}

	public Optional<Store> findByMember(final Member member) {
		return storeRepository.findByMember(member);
	}
}
