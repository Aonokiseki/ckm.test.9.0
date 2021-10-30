package com.trs.dlvrs.api.pojo;

import java.util.List;
import java.util.Map;

import com.trs.dlvrs.api.pojo.TableOcrImageResult.Result.CellResult.Location;

public class TableOcrImageResult implements Returnable{
	
	private String code;
	private String message;
	private String file_name;
	private Result result;

	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public String getResult() {
		return result.toString();
	}
	public Result fetchResult() {
		return result;
	}
	@Override
	public String getFileName() {
		return file_name;
	}
	@Override
	public String getDetails() {
		return String.format("file_name=%s, result=%s", file_name, result);
	}
	@Override
	public String toString() {
		return "TableOcrImageResult [code=" + code + ", message=" + message + ", file_name=" + file_name + ", result="
				+ result + "]";
	}

	public static class Result{
		private Map<String, Table> table_results;
		private List<CellResult> other_results;
		
		public Map<String, Table> getTable_results() {
			return table_results;
		}
		public List<CellResult> getOther_results() {
			return other_results;
		}
		@Override
		public String toString() {
			return "Result [table_results=" + table_results + ", other_results=" + other_results + "]";
		}
		public static class Table{
			private Location table_location;
			private List<CellResult> cell_results;
			
			public Location getTable_location() {
				return table_location;
			}
			public List<CellResult> getCell_results() {
				return cell_results;
			}
			@Override
			public String toString() {
				return "Table [table_location=" + table_location + ", cell_results=" + cell_results + "]";
			}
		}
		public static class CellResult{
			private Location location;
			private String text;
			private String confidence;
			
			public Location getLocation() {
				return location;
			}
			public String getText() {
				return text;
			}
			public String getConfidence() {
				return confidence;
			}
			@Override
			public String toString() {
				return "OtherResult [location=" + location + ", text=" + text + ", confidence=" + confidence + "]";
			}
			public static class Location{
				private String x;
				private String y;
				private String w;
				private String h;
				@Override
				public String toString() {
					return "Location [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
				}
			}
		}
	}
}
