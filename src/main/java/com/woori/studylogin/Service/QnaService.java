    package com.woori.studylogin.Service;

    import com.woori.studylogin.DTO.QnaDTO;
    import com.woori.studylogin.Entity.QnaEntity;
    import com.woori.studylogin.Repository.QnaRepository;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.stereotype.Service;

    import java.util.Arrays;
    import java.util.List;
    import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    public class QnaService {
        private final QnaRepository qnaRepository; //사용할 데이터베이스 테이블
        private ModelMapper modelMapper = new ModelMapper(); //변환

        //작업할 메소드를 생성해서 작업을 진행
        //삽입
        public void insert(QnaDTO qnaDTO){ //Controller에서 boardDTO값을 받아서
            //DTO->Entity로 변환 후 사용
            //boardDTO의 내용을 boardEntity형식으로 변환해서 저장
            QnaEntity qnaEntity = modelMapper.map(qnaDTO, QnaEntity.class);

            //데이터베이스에 저장(사용할 Repository.save(Entity))
            qnaRepository.save(qnaEntity);
        }
        //수정=삽입(동일한 작업), 수정은 데이터베이스에 값이 존재하고 있는지?
        public void update(QnaDTO qnaDTO){
            Optional<QnaEntity> find = qnaRepository.findById(qnaDTO.getId());
            if(find.isPresent()){
                QnaEntity qnaEntity = modelMapper.map(qnaDTO,QnaEntity.class);
                qnaRepository.save(qnaEntity);
            }
        }

        //삭제
        public void delete(Integer id){ //번호를 받아서 해당 데이터를 삭제

            qnaRepository.deleteById(id);

        }
        //상세보기
        public QnaDTO detail(Integer id){ //읽어오고 싶은 번호를 받아서 해당 데이터를 전달
            //DTO가 아니면 변환은 필요가 없다.
            Optional<QnaEntity>qnaEntity=qnaRepository.findById(id); //해당번호로 조회
            //결과 Entity를 DTO로 변환
            QnaDTO qnaDTO = modelMapper.map(qnaEntity,QnaDTO.class);

            return qnaDTO; //컨트롤러에서 사용할 DTO로 전달
        }
        //전체목록
        public List<QnaDTO> list(){ //가져오는 값은 없고, 저장된 모든 내용을 DTO로 전달, DTO가 여러개이면 LIST
            List<QnaEntity> qnaEntities = qnaRepository.findAll(); //모든 데이터(레코드)를 읽어온다.
            //System.out.println(boardEntities.stream().toList());
            //여러개의 Entity값을 여러개의 DTO로 변환 -> 여러개(Arrays.asList)
            List<QnaDTO> qnaDTOS = Arrays.asList(modelMapper.map(qnaEntities, QnaDTO[].class));

            return qnaDTOS; //컨트롤러에 값을 전달

        }
    }