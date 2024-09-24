package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.GuideDTO;
import com.woori.studylogin.Entity.GuideEntity;
import com.woori.studylogin.Repository.GuideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuideService {

    private final GuideRepository guideRepository;

    public GuideService(GuideRepository guideRepository) {
        this.guideRepository = guideRepository;
    }

    public List<GuideDTO> getAllGuides() {
        List<GuideEntity> guides = guideRepository.findAll();
        return guides.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GuideDTO getGuideById(Integer id) {
        GuideEntity guide = guideRepository.findById(id)
                .orElseThrow(() -> new GuideNotFoundException("ID " + id + "의 가이드가 없습니다."));
        return convertToDTO(guide);
    }

    public GuideDTO createGuide(GuideDTO guideDTO) {
        GuideEntity guide = new GuideEntity();
        guide.setTitle(guideDTO.getTitle());
        guide.setDescription(guideDTO.getDescription());
        guide.setProcessStep(guideDTO.getProcessStep());
        guide.setAdditionalInfo(guideDTO.getAdditionalInfo());
        guide = guideRepository.save(guide);
        return convertToDTO(guide);
    }

    public void deleteGuide(Integer id) {
        if (!guideRepository.existsById(id)) {
            throw new GuideNotFoundException("ID " + id + "의 가이드가 없습니다.");
        }
        guideRepository.deleteById(id);
    }

    private GuideDTO convertToDTO(GuideEntity guide) {
        return new GuideDTO(guide.getId(), guide.getTitle(), guide.getDescription(), guide.getProcessStep(), guide.getAdditionalInfo());
    }

    public class GuideNotFoundException extends RuntimeException {
        public GuideNotFoundException(String message) {
            super(message);
        }
    }
}
