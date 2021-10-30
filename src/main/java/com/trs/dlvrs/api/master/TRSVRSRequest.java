package com.trs.dlvrs.api.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.trs.ckm.util.HttpOperator;
import com.trs.dlvrs.api.pojo.ImageFeatureResult;
import com.trs.dlvrs.api.pojo.BasicResult;
import com.trs.dlvrs.api.pojo.CompareFaceImageResult;
import com.trs.dlvrs.api.pojo.FaceRecognitionResult;
import com.trs.dlvrs.api.pojo.GraphSearchResult;
import com.trs.dlvrs.api.pojo.IRSFeatureImageResult;
import com.trs.dlvrs.api.pojo.ImageSupportResult;
import com.trs.dlvrs.api.pojo.ModulesResult;
import com.trs.dlvrs.api.pojo.NSFWImageResult;
import com.trs.dlvrs.api.pojo.OCRImageResult;
import com.trs.dlvrs.api.pojo.TableOcrImageResult;
import com.trs.dlvrs.api.pojo.ObjectDetectionResult;
import com.trs.dlvrs.api.pojo.TerroristImageResult;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TRSVRSRequest {
	
	private String accessAddress;
	private HttpOperator httpOperator;
	private final static Gson GSON = new Gson();
	
	public TRSVRSRequest() {
		this.httpOperator = HttpOperator.get();
	}
	
	@Autowired
	public TRSVRSRequest(
			@Value("${dlvrs.host}") String host, 
			@Value("${dlvrs.port}") Integer port) {
		this();
		this.accessAddress = "http://" + host + ":" + port;
	}
	
	public String accessAddress() {
		return this.accessAddress;
	}
	/**
	 * 获取版本号
	 * @return
	 */
	public BasicResult about() {
		String url = accessAddress + URL.ABOUT;
		BasicResult result = GSON.fromJson(httpOperator.get(url), BasicResult.class);
		return result;
	}
	/**
	 * 获取功能列表
	 * @return
	 */
	public ModulesResult modules() {
		String url = accessAddress + URL.MODULES;
		ModulesResult result = GSON.fromJson(httpOperator.get(url), ModulesResult.class);
		return result;
	}
	/**
	 * 健康检查
	 * @return
	 */
	public BasicResult healthCheck() {
		String url = accessAddress + URL.HEALTH_CHECK;
		BasicResult result = GSON.fromJson(httpOperator.get(url), BasicResult.class);
		return result;
	}
	/**
	 * 查看支持的图片格式
	 * @return
	 */
	public ImageSupportResult imageSupport() {
		String url = accessAddress + URL.IMAGE_SUPPORT;
		ImageSupportResult result = GSON.fromJson(httpOperator.get(url), ImageSupportResult.class);
		return result;
	}
	/**
	 * 均值hash
	 * @param filePath
	 * @return
	 */
	public ImageFeatureResult aHashImageFile(String filePath) {
		String url = accessAddress + URL.AHASH_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * 均值hash
	 * @param remoteFilePath
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public ImageFeatureResult aHashImagePath(String remoteFilePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.AHASH_IMAGE_PATH;
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", remoteFilePath);
		return GSON.fromJson(httpOperator.post(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * 感知hash
	 * @param filePath
	 * @return
	 */
	public ImageFeatureResult pHashImageFile(String filePath) {
		String url = accessAddress + URL.PHASH_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * 感知hash
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public ImageFeatureResult pHashImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.PHASH_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * 差值hash
	 * @param filePath
	 * @return
	 */
	public ImageFeatureResult dHashImageFile(String filePath) {
		String url = accessAddress + URL.DHASH_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * 差值hash
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public ImageFeatureResult dHashImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DHASH_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * md5
	 * @param filePath
	 * @return
	 */
	public ImageFeatureResult md5ImageFile(String filePath) {
		String url = accessAddress + URL.MD5_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * md5
	 * @param filePath
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public ImageFeatureResult md5ImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.MD5_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), ImageFeatureResult.class);
	}
	/**
	 * 重启人脸库<br>
	 * @return
	 */
	public BasicResult reloadFaceDatabase() {
		String url = accessAddress + URL.RELOAD_FACE_DATABASE;
		return GSON.fromJson(httpOperator.get(url), BasicResult.class);
	}
	/**
	 * 人脸库上传
	 * @param filePath
	 * @param img_id
	 * @param face_id
	 * @param face_name
	 * @return
	 */
	public BasicResult uploadFaceFile(String filePath, String img_id, String face_id, String face_name) {
		String url = accessAddress + URL.UPLOAD_FACE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("img_id", img_id);
		parameter.add("face_id", face_id);
		parameter.add("face_name", face_name);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), BasicResult.class);
	}
	/**
	 * 人脸库上传
	 * @param filePath
	 * @param img_id
	 * @param face_id
	 * @param face_name
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public BasicResult uploadFacePath(String filePath, String img_id, String face_id, String face_name) 
			throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.UPLOAD_FACE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("img_id", img_id);
		parameter.add("face_id", face_id);
		parameter.add("face_name", face_name);
		return GSON.fromJson(httpOperator.post(url, parameter), BasicResult.class);
	}
	/**
	 * 人脸库删除记录
	 * @param img_id
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public BasicResult deleteFace(String img_id) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DELETE_FACE;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("img_id", img_id);
		return GSON.fromJson(httpOperator.post(url, parameter), BasicResult.class);
	}
	/**
	 * 人脸识别
	 * @param filePath
	 * @return
	 */
	public FaceRecognitionResult faceRecognitionImageFile(String filePath) {
		String url = accessAddress + URL.FACE_RECOGNITION_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), FaceRecognitionResult.class);
	}
	/**
	 * 人脸识别
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public FaceRecognitionResult faceRecognitionImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.FACE_RECOGNITION_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), FaceRecognitionResult.class);
	}
	/**
	 * 人脸特征提取
	 * @param filePath
	 * @return
	 */
	public FaceRecognitionResult faceFeatureImageFile(String filePath) {
		String url = accessAddress + URL.FACE_FEATURE_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), FaceRecognitionResult.class);
	}
	/**
	 * 人脸特征提取
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public FaceRecognitionResult faceFeatureImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.FACE_FEATURE_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), FaceRecognitionResult.class);
	}
	/**
	 * 人脸相似度对比
	 * @param filePath1
	 * @param filePath2
	 * @return
	 */
	public CompareFaceImageResult compareFaceImageFile(String filePath1, String filePath2) {
		String url = accessAddress + URL.COMPARE_FACE_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file1", new FileSystemResource(filePath1));
		parameter.add("file2", new FileSystemResource(filePath2));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), CompareFaceImageResult.class);
	}
	/**
	 * 人脸相似度对比
	 * @param filePath1
	 * @param filePath2
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public CompareFaceImageResult compareFaceImagePath(String filePath1, String filePath2) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.COMPARE_FACE_IMAGE_PATH;
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path1", filePath1);
		parameter.add("path2", filePath2);
		return GSON.fromJson(httpOperator.post(url, parameter), CompareFaceImageResult.class);
	}
	/**
	 * Hybase重启连接<br>
	 * 研发组似乎去掉了这个接口
	 * @return
	 * @deprecated
	 */
	public BasicResult reloadHybase() {
		String url = accessAddress + URL.RELOAD_HYBASE;
		return GSON.fromJson(httpOperator.get(url), BasicResult.class);
	}
	/**
	 * 图像特征提取
	 * @param filePath
	 * @return
	 */
	public IRSFeatureImageResult irsFeatureImageFile(String filePath) {
		String url = accessAddress + URL.IRS_FEATURE_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), IRSFeatureImageResult.class);
	}
	/**
	 * 图像特征提取
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public IRSFeatureImageResult irsFeatureImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.IRS_FEATURE_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), IRSFeatureImageResult.class);
	}
	/**
	 * 图像检索
	 * @param filePath
	 * @param show_num
	 * @param thresh
	 * @return
	 */
	public GraphSearchResult irsImageFile(String filePath, String show_num, String thresh) {
		String url = accessAddress + URL.IRS_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("show_num", show_num);
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), GraphSearchResult.class);
	}
	/**
	 * 图像检索
	 * @param filePath
	 * @param show_num
	 * @param thresh
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public GraphSearchResult irsImagePath(String filePath, String show_num, String thresh) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.IRS_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("show_num", show_num);
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.post(url, parameter), GraphSearchResult.class);
	}
	/**
	 * 插入记录
	 * @param filePath
	 * @param id
	 * @return
	 */
	public BasicResult insertImageFile(String filePath, String id) {
		String url = accessAddress + URL.INSERT_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("id", id);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), BasicResult.class);
	}
	/**
	 * 插入记录
	 * @param filePath
	 * @param id
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public BasicResult insertImagePath(String filePath, String id) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.INSERT_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("id", id);
		return GSON.fromJson(httpOperator.post(url, parameter), BasicResult.class);
	}
	/**
	 * 删除记录
	 * @param id
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public BasicResult deleteImageId(String id) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DELETE_IMAGE_ID;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("id", id);
		return GSON.fromJson(httpOperator.post(url, parameter), BasicResult.class);
	}
	/**
	 * OCR图像识别
	 * @param filePath
	 * @return
	 */
	public OCRImageResult ocrImageFile(String filePath) {
		String url = accessAddress + URL.OCR_IMAGE_FILE;
		MultiValueMap<String,Object> parameter = new LinkedMultiValueMap<String,Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), OCRImageResult.class);
	}
	/**
	 * OCR图像识别
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public OCRImageResult ocrImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.OCR_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), OCRImageResult.class);
	}
	/**
	 * OCR表格图像识别
	 * @param filePath
	 * @return
	 */
	public TableOcrImageResult tableOcrImageFile(String filePath) {
		String url = accessAddress + URL.TABLE_OCR_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), TableOcrImageResult.class);
	}
	/**
	 * OCR表格图像识别
	 * @param filePath
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public TableOcrImageResult tableOcrImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.TABLE_OCR_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), TableOcrImageResult.class);
	}
	/**
	 * NSFW图像鉴别
	 * @param filePath
	 * @return
	 */
	public NSFWImageResult nsfwImageFile(String filePath) {
		String url = accessAddress + URL.NSFW_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), NSFWImageResult.class);
	}
	/**
	 * NSFW图像鉴别
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws JsonSyntaxException 
	 */
	public NSFWImageResult nsfwImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.NSFW_IMAGE_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), NSFWImageResult.class);
	}
	/**
	 * 暴恐图片识别
	 * @param filePath
	 * @return
	 */
	public TerroristImageResult terroristImageFile(String filePath) {
		String url = accessAddress + URL.TERRORIST_IMAGE_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		return GSON.fromJson(httpOperator.specialPost(url, parameter), TerroristImageResult.class); 
	}
	/**
	 * 暴恐图片识别
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public TerroristImageResult terroristImagePath(String filePath) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.TERRORIST_IMAGE_PATH;
		MultiValueMap<String,String> parameter = new LinkedMultiValueMap<String,String>();
		parameter.add("path", filePath);
		return GSON.fromJson(httpOperator.post(url, parameter), TerroristImageResult.class);
	}
	/**
	 * 旗帜检测
	 * @param filePath
	 * @return
	 */
	public ObjectDetectionResult detectionFlagFile(String filePath, String thresh) {
		String url = accessAddress + URL.DETECTION_FLAG_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * 旗帜检测
	 * @param filePath
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public ObjectDetectionResult detectionFlagPath(String filePath, String thresh) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DETECTION_FLAG_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.post(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * 商品logo检测
	 * @param filePath
	 * @param thresh
	 * @return
	 */
	public ObjectDetectionResult detectionLogoFile(String filePath, String thresh) {
		String url = accessAddress + URL.DETECTION_LOGO_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * 商品logo检测
	 * @param filePath
	 * @param thresh
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public ObjectDetectionResult detectionLogoPath(String filePath, String thresh) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DETECTION_LOGO_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.post(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * 电视台标检测
	 * @param filePath
	 * @param thresh
	 * @return
	 */
	public ObjectDetectionResult detectionTvFile(String filePath, String thresh) {
		String url = accessAddress + URL.DETECTION_TV_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * 电视台标检测
	 * @param filePath
	 * @param thresh
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public ObjectDetectionResult detectionTvPath(String filePath, String thresh) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DETECTION_TV_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.post(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * coco目标检测
	 * @param filePath
	 * @param thresh
	 * @return
	 */
	public ObjectDetectionResult detectionCocoFile(String filePath, String thresh) {
		String url = accessAddress + URL.DETECTION_COCO_FILE;
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String, Object>();
		parameter.add("file", new FileSystemResource(filePath));
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.specialPost(url, parameter), ObjectDetectionResult.class);
	}
	/**
	 * coco目标检测
	 * @param filePath
	 * @param thresh
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public ObjectDetectionResult detectionCocoPath(String filePath, String thresh) throws JsonSyntaxException, IOException {
		String url = accessAddress + URL.DETECTION_COCO_PATH;
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<String, String>();
		parameter.add("path", filePath);
		parameter.add("thresh", thresh);
		return GSON.fromJson(httpOperator.post(url, parameter), ObjectDetectionResult.class);
	}
}
