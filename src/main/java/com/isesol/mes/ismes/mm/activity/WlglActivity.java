package com.isesol.mes.ismes.mm.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.isesol.ismes.platform.core.service.bean.Dataset;
import com.isesol.ismes.platform.module.Bundle;
import com.isesol.ismes.platform.module.Parameters;
import com.isesol.ismes.platform.module.Sys;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WlglActivity {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf_time = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 跳转物料管理
	 * 
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public String query_wlgl(Parameters parameters, Bundle bundle) {
		return "mm_wlgl";
	}
	/**跳转刀具管理
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public String query_djgl(Parameters parameters, Bundle bundle) {
		return "mm_djgl";
	}
	/**跳转工装夹具管理
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public String query_gzlj(Parameters parameters, Bundle bundle) {
		return "mm_gzlj";
	}
	
	
	/**查询物料列表
	 * @param parameters
	 * @param bundle
	 * @return
	 * @throws Exception 
	 */
	public String table_wlgl(Parameters parameters, Bundle bundle) throws Exception {

		String query_wlbh = parameters.getString("query_wlbh"); //  物料编号
		String query_wlmc = parameters.getString("query_wlmc"); //  物料名称
		String query_wlpp = parameters.getString("query_wlpp"); //  物料编号
		String query_wltm = parameters.getString("query_wltm"); //  物料名称
		String query_wllxdm = parameters.getString("query_wllxdm"); // 物料类型代码
		String query_wllbdm = parameters.getString("query_wllbdm"); // 物料类别代码
		String query_qyzt = parameters.getString("query_qyzt"); // 	   启用状态
		String query_jgzt = parameters.getString("query_jgzt"); // 	   加工状态
		String sortName = parameters.getString("sortName");// 排序字段
		String sortOrder = parameters.getString("sortOrder");// 排序方式 asc  desc		
		String query_yxq = parameters.getString("query_yxq");//有效期
		String query_aqkc = parameters.getString("query_aqkc");//安全库存
		String wllbdm = parameters.getString("wllb");// 排序方式 asc  desc
		String query_wlgg = parameters.getString("query_wlgg"); // 	   刀具型号
		
		String con = "1 = 1 and wllbdm in  "+wllbdm;
		List<Object> val = new ArrayList<Object>();
		if(StringUtils.isNotBlank(query_wlbh)) 
		{
			con = con + " and wlbh like ? ";
			val.add("%"+query_wlbh+"%");
		}
		if(StringUtils.isNotBlank(query_wlmc)) 
		{
			con = con + " and wlmc like ? ";
			val.add("%"+query_wlmc+"%");
		}
		if(StringUtils.isNotBlank(query_wlpp)) 
		{
			con = con + " and wlpp like ? ";
			val.add("%"+query_wlpp+"%");
		}
		if(StringUtils.isNotBlank(query_wltm)) 
		{
			con = con + " and wltm like ? ";
			val.add("%"+query_wltm+"%");
		}
		if(StringUtils.isNotBlank(query_wllxdm))
		{
			con = con + " and wllxdm = ? ";
			val.add(query_wllxdm);
		}
		if(StringUtils.isNotBlank(query_wllbdm))
		{
			con = con + " and wllbdm = ? ";
			val.add(query_wllbdm);
		}
		if(StringUtils.isNotBlank(query_qyzt))
		{
			con = con + " and wlzt = ? ";
			val.add(query_qyzt);
		}
		if(StringUtils.isNotBlank(query_jgzt))
		{
			con = con + " and jgztdm = ? ";
			val.add(query_jgzt);
		}
		if(StringUtils.isNotBlank(query_yxq))
		{
			con = con + " and yxq = ? ";
			val.add(query_yxq);
		}
		if(StringUtils.isNotBlank(query_aqkc))
		{
			con = con + " and aqkc = ? ";
			val.add(query_aqkc);
		}
		//刀具型号
		if (StringUtils.isNotBlank(query_wlgg)){
			con = con + " and wlgg like ? ";
			val.add("%"+query_wlgg+"%");
		}
		if(StringUtils.isNotBlank(sortName))
		{
			sortOrder = sortName + " "+sortOrder+" ";
		}else {
			sortOrder = null;
		}
			
		int page = Integer.parseInt(parameters.get("page").toString());
		int pageSize = Integer.parseInt(parameters.get("pageSize").toString());
		Dataset datasetWlgl = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz,wlpp,yxq,sysm,aqkc,jgztdm", con, "wlbh", (page-1)*pageSize, pageSize,val.toArray());
		List<Map<String, Object>> wlxx = datasetWlgl.getList();
		bundle.put("rows", wlxx);
		int totalPage = datasetWlgl.getTotal()%pageSize==0?datasetWlgl.getTotal()/pageSize:datasetWlgl.getTotal()/pageSize+1;
		bundle.put("totalPage", totalPage);
		bundle.put("currentPage", page);
		bundle.put("totalRecord", datasetWlgl.getTotal());
		return "json:";
	}
	
	/**插入/修改物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void update_wlxx(Parameters parameters, Bundle bundle) {
		
		List<Map<String,Object>> wlgl_inlist = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> wlgl_uplist = new ArrayList<Map<String,Object>>();
		List<Object[]>  objlist= new ArrayList<Object[]>();
		JSONObject jsonObject = JSONObject.fromObject(parameters.get("data_list"));
		Map<String, Object> map = (Map<String, Object>) jsonObject;
		
		if(StringUtils.isBlank(map.get("wlid").toString()))
		{
			map.remove("wlid");
			if(map.get("wlcb") == null || "".equals(map.get("wlcb")+"")){
				map.put("wlcb", 0);
			}
			wlgl_inlist.add(map);
			try {
				int i = Sys.insert("mm_wlb", wlgl_inlist);
				System.out.println("插入数量"+i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			objlist.add(new Object[]{map.get("wlid")});
			map.remove("wlid");
			if(map.get("wlcb") == null || "".equals(map.get("wlcb")+"")){
				map.put("wlcb", 0);
			}
			wlgl_uplist.add(map); 
			try {
				int i = Sys.update("mm_wlb",wlgl_uplist,"wlid=?",objlist);
				System.out.println("更新数量"+i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public String getWlxxByWlgg(Parameters parameters, Bundle bundle){
		
		String wlgg = parameters.getString("wlgg");
		
		Dataset dataset = Sys.query("mm_wlb", "jgztdm,wlgg,wlid", "wlgg = ?", null, new Object[]{wlgg});
		
		bundle.put("list", dataset.getList());
		
		return "json:";
	}
	
	/**插入/修改物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void update_wlxx_upload(Parameters parameters, Bundle bundle) {
		
		List<Map<String,Object>> wlgl_inlist = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> wlgl_uplist = new ArrayList<Map<String,Object>>();
		List<Object[]>  objlist= new ArrayList<Object[]>();
		//20161105 modify by maww 上次刀具图片，变更数据提交/接收方式
//		JSONObject jsonObject = JSONObject.fromObject(parameters.get("data_list"));
//		Map<String, Object> map = (Map<String, Object>) jsonObject;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wlbh", parameters.get("add_wlbh").toString());	//物料编号
		map.put("wlmc", parameters.get("add_wlmc").toString());	//物料名称
		map.put("wlgg", parameters.get("add_wlgg").toString());	//物料规格
		if(null != parameters.get("add_wlcb") && !"".equals(parameters.get("add_wlcb").toString())){
			map.put("wlcb", Double.parseDouble(parameters.get("add_wlcb").toString().trim()));	//物料成本	
		}
		map.put("wllxdm", parameters.get("add_wllxdm").toString());	//物料类型
		map.put("wllbdm", parameters.get("add_wllbdm").toString());	//物料类别
		map.put("wlpp", parameters.get("add_wlpp").toString());	//物料品牌
		map.put("wltm", parameters.get("add_wltm").toString());	//物料类型条码
		map.put("sysm", parameters.get("add_sysm").toString());	//使用寿命
		map.put("aqkc", parameters.get("add_aqkc").toString());	//安全库存
		map.put("wldwdm", parameters.get("add_wljldw").toString());	//物料单位
		map.put("wlzt", parameters.get("add_qyzt").toString());	//物料状态
		map.put("bz", parameters.get("add_bz").toString());	//备注
		if (null != parameters.getString("add_wlid") && !"".equals(parameters.getString("add_wlid").toString().trim())){
			map.put("wlid", Integer.parseInt(parameters.getString("add_wlid").toString()));	//物料ID	
		}
		/*
		if (null != parameters.get("add_yxq")){
			map.put("yxq", parameters.get("add_yxq").toString());	//有效期	
		}
		*/
		
		//20161105 add by maww 上次图片
		if (null != parameters.getFile("add_djtp")){
			String cxmc = parameters.getFile("add_djtp").getName();
			String ContentType = parameters.getFile("add_djtp").getContentType();
			long   wjdx = parameters.getFile("add_djtp").getSize();
			String filetype = cxmc.substring((cxmc.lastIndexOf(".")),cxmc.length());
			String wjbcmc="wlgl" + sdf_time.format(new Date())+filetype;
			Map<String, Object> mapfile = new HashMap<String, Object>(); 
			mapfile.put("wjlj", "/wlgl/"+wjbcmc);
			mapfile.put("wjmc", wjbcmc);
			mapfile.put("wjdx", wjdx);
			mapfile.put("wjlb", ContentType);
			parameters.set("infoMap", mapfile); 
			Sys.saveFile("/wlgl/"+wjbcmc, parameters.getFile("add_djtp").getInputStream());
			Bundle b_file = Sys.callModuleService("pm", "pmservice_insertFile", parameters);
			Map<String, Object> sbmap = new HashMap<String, Object>(); 
			
			int wjid = Integer.parseInt(b_file.get("wjid").toString());
			map.put("wltpid", wjid);
		}
		
		if(null == map.get("wlid") || StringUtils.isBlank(map.get("wlid").toString()))
		{
			map.remove("wlid");
			wlgl_inlist.add(map);
			try {
				int i = Sys.insert("mm_wlb", wlgl_inlist);
				System.out.println("插入数量"+i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			objlist.add(new Object[]{map.get("wlid")});
			map.remove("wlid");
			wlgl_uplist.add(map); 
			try {
				int i = Sys.update("mm_wlb",wlgl_uplist,"wlid=?",objlist);
				System.out.println("更新数量"+i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**删除物料信息
	 * @param parameters
	 * @param bundle
	 */
	@SuppressWarnings("unchecked")
	public void del_wlxx(Parameters parameters, Bundle bundle) {
		List<Object[]>  objlist= new ArrayList<Object[]>();
		JSONArray jsonarray = JSONArray.fromObject(parameters.get("data_list"));  
		for (int i = 0; i < jsonarray.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>(); 
			map = jsonarray.getJSONObject(i);
			objlist.add(new Object[]{Integer.parseInt(map.get("wlid").toString())});
		}  
		try {
			int i = Sys.delete("mm_wlb", "wlid = ? ", objlist);
			System.out.println("删除数量----"+i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String wlggSelect(Parameters parameters,Bundle bundle){
		
		String query = parameters.get("query").toString();
		
		Dataset dataset = Sys.query("mm_wlgg","ggid,ggpid,ggname,ggvalue", 
				"ggname like '%"+query+"%' or ggvalue like '%" +query+"%'", 
				null, new Object[]{});
		
		List<Map<String, Object>> list = dataset.getList();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("label", list.get(i).get("ggvalue"));
			list.get(i).put("value", list.get(i).get("ggvalue"));
			list.get(i).put("title", list.get(i).get("ggvalue"));
		}		
		bundle.put("list", list);
		return "json:list";
	}
}




