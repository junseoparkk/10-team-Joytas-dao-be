package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeDetailResponseDto;
import com.example.daobe.lounge.application.dto.LoungeInfoResponseDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.event.LoungeDeletedEvent;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeService {

    private final LoungeRepository loungeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Lounge createAndSaveLounge(LoungeCreateRequestDto request, User user) {
        Lounge lounge = Lounge.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .build();
        loungeRepository.save(lounge);
        return lounge;
    }

    public List<LoungeInfoResponseDto> getLoungeInfosByUserId(Long userId) {
        return loungeRepository.findLoungeByUserId(userId).stream()
                .filter(Lounge::isActive)
                .map(LoungeInfoResponseDto::of)
                .toList();
    }

    public LoungeDetailResponseDto getLoungeDetailInfo(Lounge lounge) {
        lounge.isActiveOrThrow();
        return LoungeDetailResponseDto.of(lounge);
    }

    public Lounge getLoungeById(Long loungeId) {
        return loungeRepository.findById(loungeId)
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_ID_EXCEPTION));
    }

    public void deleteLoungeByUserId(Long userId, Lounge lounge) {
        lounge.softDelete(userId);
        eventPublisher.publishEvent(new LoungeDeletedEvent(lounge.getId()));
    }
}
