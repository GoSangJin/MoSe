import os, shutil
from flask import Flask, request
from yolov5 import detect as Detect
import base64
import json
prev_id = ''
app = Flask(__name__)
#매핑명 "/detect", 전송방식 "POST"일 경우
@app.route('/detect', methods=['POST'])
def detect_target():
    global prev_id
    #전달받은 파일명과 바이트형식을 처리
    # file_name = request.form.get('filename') 전달받은 이름으로 저장을 할 때
    file_name = 'test'+(request.form.get('extension')) #동일한 이름으로 저장할 때
    img = request.form.get('image')
    ret_img_string = img
    img = base64.b64decode(ret_img_string)
    #이미지 분석을 위해 flask서버에 이미지를 임시 복사
    #리눅스 서버시 base_directory = "/home/ubuntu..."
    base_directory = "c:/AI/image"
    file_path = os.path.join(base_directory, file_name)
    #img.save(file_path) #분석을 위한 이미지파일 임시 저장(1)
    #분석을 위한 이미지파일 임시 저장(2)
    with open(file_path, 'wb') as file:
        file.write(img)
    file.close()

    #결과를 저장할 폴더
    #결과값 폴더를 임의로 작성할 때(자바로부터 임의의 폴더명을 받아 온다.)
    #uuid = request.form.get('uuid')
    #print(file_name)
    uuid = "result" #고정된 폴더명을 사용할 때
    ret_path = '/AI/yolov5/runs/detect/result' #결과가 저장되는 위치
    if os.path.isdir(ret_path): #기존 폴더가 존재하면 삭제처리
        shutil.rmtree(ret_path)
    Detect.detect(file_path, True, True, uuid) #이미지 분석
    #폴더 조회(폴더이름을 분류명으로 처리해서 전달)
    class_path = ret_path+'/crops/'
    class_list = os.listdir(class_path)
    #print(class_list)
    #결과 이미지 읽기
    ret_file = open(ret_path+'/'+file_name, 'rb')
    image_binary = ret_file.read()
    encoded_string = base64.b64encode(image_binary)
    ret_file.close()
    image_dict = {'image': encoded_string.decode(), 'class':class_list}
    image_json = json.dumps(image_dict)
    return image_json
    #prev_id = uuid
    #return 'Hello Flask World'
if __name__ == '__main__':
    app.run(host='0.0.0.0')