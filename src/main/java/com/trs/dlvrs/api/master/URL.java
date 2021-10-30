package com.trs.dlvrs.api.master;

public class URL {
	private URL() {}
	
	/** 版本号 */
	public final static String ABOUT = "/DL-VRS/about";
	/** 功能概述 */
	public final static String MODULES = "/DL-VRS/modules";
	/** 健康检查 */
	public final static String HEALTH_CHECK = "/DL-VRS/healthcheck";
	/** 查看支持的图片格式 */
	public final static String IMAGE_SUPPORT = "/DL-VRS/image/support";
	
	/** 均值hash */
	public final static String AHASH_IMAGE_FILE = "/ahash/image/file";
	/** 均值hash */
	public final static String AHASH_IMAGE_PATH = "/ahash/image/path";
	/** 感知hash */
	public final static String PHASH_IMAGE_FILE = "/phash/image/file";
	/** 感知hash */
	public final static String PHASH_IMAGE_PATH = "/phash/image/path";
	/** 差值hash */
	public final static String DHASH_IMAGE_FILE = "/dhash/image/file";
	/** 差值hash */
	public final static String DHASH_IMAGE_PATH = "/dhash/image/path";
	/** MD5 */
	public final static String MD5_IMAGE_FILE = "/md5/image/file";
	/** MD5	*/
	public final static String MD5_IMAGE_PATH = "/md5/image/path";
	
	/** 人脸库重启 */
	public final static String RELOAD_FACE_DATABASE = "/reload/face/database";
	/** 人脸库上传 */
	public final static String UPLOAD_FACE_FILE = "/upload/face/file";
	/** 人脸库上传 */
	public final static String UPLOAD_FACE_PATH = "/upload/face/path";
	/** 人脸库删除 */
	public final static String DELETE_FACE = "/delete/face";
	/** 人脸识别 */
	public final static String FACE_RECOGNITION_IMAGE_FILE = "/face_recognition/image/file";
	/** 人脸识别 */
	public final static String FACE_RECOGNITION_IMAGE_PATH = "/face_recognition/image/path";
	/** 人脸特征提取 */
	public final static String FACE_FEATURE_IMAGE_FILE = "/face_feature/image/file";
	/** 人脸特征提取 */
	public final static String FACE_FEATURE_IMAGE_PATH = "/face_feature/image/path";
	/** 人脸相似度比对 */
	public final static String COMPARE_FACE_IMAGE_FILE = "/compare_face/image/file";
	/** 人脸相似度比对 */
	public final static String COMPARE_FACE_IMAGE_PATH = "/compare_face/image/path";
	
	/** Hybase重启连接 */
	public final static String RELOAD_HYBASE = "/reload/hybase";
	/** 图像特征提取 */
	public final static String IRS_FEATURE_IMAGE_FILE = "/irs_feature/image/file";
	/** 图像特征提取 */
	public final static String IRS_FEATURE_IMAGE_PATH = "/irs_feature/image/path";
	/** 图像检索 */
	public final static String IRS_IMAGE_FILE = "/irs/image/file";
	/** 图像检索 */
	public final static String IRS_IMAGE_PATH = "/irs/image/path";
	/** 插入记录 */
	public final static String INSERT_IMAGE_FILE = "/insert/image/file";
	/** 插入记录*/
	public final static String INSERT_IMAGE_PATH = "/insert/image/path";
	/** 删除记录 */
	public final static String DELETE_IMAGE_ID = "/delete/image/id";
	
	/** OCR图像识别 */
	public final static String OCR_IMAGE_FILE = "/ocr/image/file";
	/** OCR图像识别 */
	public final static String OCR_IMAGE_PATH = "/ocr/image/path";
	/** OCR表格图像识别 */
	public final static String TABLE_OCR_IMAGE_FILE = "/table_ocr/image/file";
	/** OCR表格图像识别 */
	public final static String TABLE_OCR_IMAGE_PATH = "/table_ocr/image/path";
	
	/** NSFW图像鉴别 */
	public final static String NSFW_IMAGE_FILE = "/nsfw/image/file";
	/** NSFW图像鉴别 */
	public final static String NSFW_IMAGE_PATH = "/nsfw/image/path";
	/** 暴恐图像识别 */
	public final static String TERRORIST_IMAGE_FILE = "/terrorist/image/file";
	/** 暴恐图像识别 */
	public final static String TERRORIST_IMAGE_PATH = "/terrorist/image/path";
	
	/** 旗帜检测*/
	public final static String DETECTION_FLAG_FILE = "/detection/flag/file";
	/** 旗帜检测 */
	public final static String DETECTION_FLAG_PATH = "/detection/flag/path";
	/** 商品logo检测 */
	public final static String DETECTION_LOGO_FILE = "/detection/logo/file";
	/** 商品logo检测 */
	public final static String DETECTION_LOGO_PATH = "/detection/logo/path";
	/** 电视台标检测 */
	public final static String DETECTION_TV_FILE = "/detection/tv/file";
	/** 电视台标检测 */
	public final static String DETECTION_TV_PATH = "/detection/tv/path";
	/** coco目标检测 */
	public final static String DETECTION_COCO_FILE = "/detection/coco/file";
	/** coco目标检测 */
	public final static String DETECTION_COCO_PATH = "/detection/coco/path";
}
