package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.ReportDTO;
import com.woori.studylogin.Entity.BoardEntity;
import com.woori.studylogin.Entity.ReportEntity;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.BoardRepository;
import com.woori.studylogin.Repository.ReportRepository;
import com.woori.studylogin.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void saveReport(ReportDTO reportDTO) {
        log.debug("DTO Reporter Username: {}", reportDTO.getReporterUsername()); // 디버깅용 로그

        BoardEntity boardEntity = boardRepository.findById(reportDTO.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        UserEntity userEntity = userRepository.findByUsername(reportDTO.getReporterUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ReportEntity reportEntity = modelMapper.map(reportDTO, ReportEntity.class);
        reportEntity.setBoard(boardEntity);
        reportEntity.setUser(userEntity);

        reportRepository.save(reportEntity);
    }

    @Transactional(readOnly = true)
    public Page<ReportDTO> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable).map(this::convertToDTO);
    }

    private ReportDTO convertToDTO(ReportEntity reportEntity) {
        ReportDTO dto = modelMapper.map(reportEntity, ReportDTO.class);
        dto.setReporterUsername(reportEntity.getUser().getUsername()); // 사용자 이름 설정
        return dto;
    }

    @Transactional(readOnly = true)
    public ReportDTO getReportById(Integer reportId) {
        ReportEntity reportEntity = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));
        return convertToDTO(reportEntity);
    }

    @Transactional
    public void handleReport(Integer reportId, String suspensionType) throws IOException {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));

        UserEntity user = report.getUser();

        if (user != null) {
            LocalDate endDate = calculateSuspensionEndDate(suspensionType);
            user.setSuspended(true);
            user.setSuspensionEndDate(endDate);
            userRepository.save(user);

            if (report.getBoard() != null) {
                reportRepository.deleteAllByBoardId(report.getBoard().getId());
                // 게시글 삭제 메서드 호출
            }

            reportRepository.delete(report);
        }
    }

    private LocalDate calculateSuspensionEndDate(String suspensionType) {
        LocalDate today = LocalDate.now();
        switch (suspensionType) {
            case "3일":
                return today.plusDays(3);
            case "7일":
                return today.plusDays(7);
            case "한달":
                return today.plusMonths(1);
            case "영구정지":
                return null; // 영구 정지의 경우 종료일 설정하지 않음
            default:
                throw new IllegalArgumentException("Invalid suspension type");
        }
    }
}
