package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.ReportDTO;
import com.woori.studylogin.Entity.BoardEntity;
import com.woori.studylogin.Entity.ReportEntity;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.BoardRepository;
import com.woori.studylogin.Repository.ReportRepository;
import com.woori.studylogin.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BoardService boardService;

    @Transactional
    public void saveReport(ReportDTO reportDTO) {
    System.out.println("DTO Reporter Username: " + reportDTO.getReporterUsername()); // 디버깅용 로그


    Optional<BoardEntity> boardOptional = boardRepository.findById(reportDTO.getBoardId());
    BoardEntity boardEntity = boardOptional.orElseThrow(() -> new RuntimeException("Board not found"));


    Optional<UserEntity> userOptional = userRepository.findByUsername(reportDTO.getReporterUsername());
    UserEntity userEntity = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

    ReportEntity reportEntity = ReportEntity.builder()
            .title(reportDTO.getTitle())
            .description(reportDTO.getDescription())
            .board(boardEntity)
            .user(userEntity)
            .build();
    reportRepository.save(reportEntity);
}

    @Transactional(readOnly = true)
    public List<ReportDTO> getAllReports() {
        List<ReportEntity> reports = reportRepository.findAll();
        return reports.stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    private ReportDTO convertToDTO(ReportEntity reportEntity) {
        ReportDTO dto = new ReportDTO();
        dto.setId(reportEntity.getId());
        dto.setTitle(reportEntity.getTitle());
        dto.setBoardId(reportEntity.getBoard().getId());
        dto.setDescription(reportEntity.getDescription());
        dto.setBoardTitle(reportEntity.getBoard().getTitle());
        dto.setBoardAuthor(reportEntity.getBoard().getAuthor());
        dto.setReporterUsername(reportEntity.getUser().getUsername());
        return dto;

    }

    public ReportDTO getReportById(Integer reportId) {
        ReportEntity reportEntity = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));

        ReportDTO reportDTO = modelMapper.map(reportEntity, ReportDTO.class);
        reportDTO.setReporterUsername(reportEntity.getUser().getUsername()); // 사용자 이름 설정

        return reportDTO;
    }


    @Transactional
    public void handleReport(Integer reportId, String suspensionType) throws IOException {
        // ReportEntity 찾기
        ReportEntity report = reportRepository.findById(reportId)
                                             .orElseThrow(() -> new RuntimeException("Report not found"));

        // 관련 UserEntity 찾기
        UserEntity user = report.getUser(); // assuming getUser() is available in ReportEntity

        if (user != null) {
            // Suspension Type에 따른 처리
            LocalDate endDate = calculateSuspensionEndDate(suspensionType);

            // UserEntity 업데이트
            user.setSuspended(true);
            user.setSuspensionEndDate(endDate);

            // 저장
            userRepository.save(user);

            // 신고 처리 후 관련 게시글 삭제
            if (report.getBoard() != null) {
                // 게시글에 대한 모든 신고 삭제
                reportRepository.deleteAllByBoardId(report.getBoard().getId());
                // 게시글 삭제
                boardService.delete(report.getBoard().getId());
            }

            // 신고 처리 후 ReportEntity 삭제
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

    private UserEntity getUserByReportId(Integer reportId) {
        // 신고된 사용자 찾는 로직 구현
        return reportRepository.findById(reportId)
                .map(ReportEntity::getUser)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 신고를 찾을 수 없습니다."));
    }
}