package com.isesol.mes.ismes.mm.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.isesol.ismes.platform.core.service.bean.Dataset;
import com.isesol.ismes.platform.module.Bundle;
import com.isesol.ismes.platform.module.Parameters;
import com.isesol.ismes.platform.module.Sys;

import net.sf.json.JSONArray;

/**
 * 工装管理
 * @author Yang Fan
 *
 */
public class GzglActivity {

	private Logger LOGGER = Logger.getLogger(this.getClass());
	/**
	 * 显示工装管理界面
	 * 
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public String query_gzgl(Parameters parameters, Bundle bundle) {
		return "mm_gzgl";
	}
	
	
	/**
	 * 显示工装管理界面
	 * 
	 * @param parameters 
	 * @param bundle
	 * @return
	 */
	public String table_gzgl(Parameters parameters, Bundle bundle) throws Exception {
		
		String query_gzbh = parameters.getString("query_gzbh"); //  工装编号
		String query_wllx = parameters.getString("query_wllx"); //  物料类型
		String query_wlbh = parameters.getString("query_wlbh"); // 物料类型代码
		String query_gzgg = parameters.getString("query_gzgg"); // 物料类别代码
		String query_gzmc = parameters.getString("query_gzmc"); // 	   启用状态
		String sortName = parameters.getString("sortName");// 排序字段
		String sortOrder = parameters.getString("sortOrder");// 排序方式 asc  desc
		
		String con = "1 = 1 ";
		List<Object> val = new ArrayList<Object>();
		if(StringUtils.isNotBlank(query_gzbh)) 
		{
			con = con + " and gzbh like ? ";
			val.add("%"+query_gzbh+"%");
		}
		if(StringUtils.isNotBlank(query_wllx)) 
		{
			con = con + " and wllbdm = ? ";
			val.add(query_wllx);
		}
		if(StringUtils.isNotBlank(query_wlbh))
		{
			LOGGER.info("query_wlbh " + query_wlbh);
			con = con + " and wlid = ? ";
			val.add(query_wlbh);
		}
		if(StringUtils.isNotBlank(query_gzgg))
		{
			con = con + " and wlgg = ? ";
			val.add(query_gzgg);
		}
		if(StringUtils.isNotBlank(query_gzmc))
		{
			con = con + " and wlmc like ? ";
			val.add("%"+query_gzmc+"%");
		}
		
		LOGGER.info(con + " " +val.size());
		
		// 查询库存信息
		int page = Integer.parseInt(parameters.get("page").toString());
		int pageSize = Integer.parseInt(parameters.get("pageSize").toString());
		//工装ID，工装编号，工装类型编号，物料编号，物料规格、物料名称、成本
		Dataset datasetgzgl = Sys.query(new String[] {"mm_gzb","mm_wlb"}, "mm_gzb left join mm_wlb on mm_gzb.gzlxbh=mm_wlb.wlid ",
				" gzid, gzbh, wlid, gzlxbh, wlbh, wlgg, wlmc, wlcb, wllbdm ", con, " gzid desc ", (page - 1) * pageSize, pageSize, val.toArray());
		List<Map<String, Object>> gzgl = datasetgzgl.getList();
		bundle.put("rows", gzgl);
		int totalPage = datasetgzgl.getTotal() % pageSize == 0 ? datasetgzgl.getTotal() / pageSize :datasetgzgl.getTotal() / pageSize + 1;
		bundle.put("totalPage", totalPage);
		bundle.put("currentPage", page);
		bundle.put("totalRecord", datasetgzgl.getTotal());
		return "json:";
	}
	
	/**
	 * 删除工装信息
	 * 
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public String del_gzxx(Parameters parameters, Bundle bundle)  throws Exception{
		if (StringUtils.isBlank(parameters.getString("data_list"))) {
			return "json:";
		}
		List<Object[]> objlist = new ArrayList<Object[]>();
		objlist.add(new Object[] { Integer.parseInt(parameters.getString("data_list"))});
		try {
			int i = Sys.delete("mm_gzb", "gzid=?", objlist);
			LOGGER.info("删除工装信息 数量：" + i + " 工装id：" + String.valueOf(objlist.get(0)[0]));
		} catch (Exception e) {
			e.printStackTrace();
			throw  new Exception("删除工装信息失败！",e);
		}
		return "json:";
	}
	
	/**
	 *  根据物料列别，查询物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 * @throws Exception
	 */
	public String changeWllb(Parameters parameters, Bundle bundle)  throws Exception{
		String parentId = parameters.getString("parent");
		LOGGER.info(parentId);
		
		//关联物料信息
		Dataset datasetWl = Sys.query("mm_wlb", " wlid, wlbh, wlgg, wlmc, wlcb, wllbdm ", " wllbdm=? ", " wlid desc ", new Object[] {parentId});
		List<Map<String, Object>> dict = datasetWl.getList();
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> map : dict){
			Map<String, Object> record = new HashMap<String, Object>();
			record.put("label", map.get("wlbh"));
			record.put("value", map.get("wlid"));
			returnList.add(record);
		}
		bundle.put("data", returnList);	
		
		return "json:data";
	}
	
	/**
	 *  根据物料列别，查询物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 * @throws Exception
	 */
	public String queryWlxx(Parameters parameters, Bundle bundle)  throws Exception{
		String wlid = parameters.getString("wlid");
		LOGGER.info("wlid " + wlid);
		
		//关联物料信息
		Dataset datasetWl = Sys.query("mm_wlb", " wlid, wlbh, wlgg, wlmc, wlcb, wllbdm ", " wlid=? ", null, new Object[] {wlid});
		Map<String, Object> data = datasetWl.getMap();
		
		bundle.put("data", data);	
		
		return "json:data";
	}
	
	
	/**
	 * 新增或者修改工装信息
	 * 
	 * @param parameters
	 * @param bundle
	 * @return
	 * @throws Exception
	 */
	public String update_gzxx(Parameters parameters, Bundle bundle) throws Exception {

		List<Map<String, Object>> gzxx_inlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> gzxx_uplist = new ArrayList<Map<String, Object>>();
		List<Object[]> objlist = new ArrayList<Object[]>();
		JSONArray jsonarray = JSONArray.fromObject(parameters.get("data_list"));
		for (int i = 0; i < jsonarray.size(); i++) {
			Map<String, Object> map = jsonarray.getJSONObject(i);
			Map<String, Object> map_in = new HashMap<String, Object>();
			
			if (StringUtils.isNotBlank(map.get("addSign").toString())) {
				//判断工装编号是否重复，重复提示用户
				Dataset ds = Sys.query("mm_gzb", "gzbh", "gzbh=?", null, new Object[]{map.get("gzbh")});
				if(null != ds && ds.getCount() > 0){
					bundle.put("code", "1");//0:成功，1:失败
					bundle.put("message", "工装编号重复，请重新输入！");
					return "json:";
				}
//				map_in.put("gzid", map.get("gzid"));
				map_in.put("gzbh", map.get("gzbh"));
				map_in.put("gzlxbh", map.get("gzlxbh"));

				gzxx_inlist.add(map_in);
			} else {
				Dataset ds = Sys.query("mm_gzb", "gzbh", "gzbh=? and gzid<>?", null, new Object[]{map.get("gzbh"),map.get("gzid")});
				if(null != ds && ds.getCount() > 0){
					bundle.put("code", "1");//0:成功，1:失败
					bundle.put("message", "工装编号重复，请重新输入！");
					return "json:";
				}
				
				objlist.add(new Object[] { map.get("gzid") });
//				map_in.put("gzid", map.get("gzid"));
				map_in.put("gzbh", map.get("gzbh"));
				map_in.put("gzlxbh", map.get("gzlxbh"));
				gzxx_uplist.add(map_in);
			}
		}
		if (gzxx_inlist.size() > 0) {
			try {
				int i = Sys.insert("mm_gzb", gzxx_inlist);
				System.out.println("新增工装信息 数量：" + i );
			} catch (Exception e) {
				e.printStackTrace();
				throw  new Exception("新增工装信息失败！",e);
			}
		}
		if (gzxx_uplist.size() > 0) {
			try {
				int i = Sys.update("mm_gzb", gzxx_uplist, "gzid=?", objlist);
				System.out.println("更新工装信息 数量：" + i );
			} catch (Exception e) {
				e.printStackTrace();
				throw  new Exception("更新工装信息失败！",e);
			}
		}
		bundle.put("code", "0");//0:成功，1:失败
		return "json:";
	}
	
	/**
	 * 模糊查询工装编号
	 * 
	 * @param parameters
	 * @param bundle
	 * @return
	 * @throws Exception
	 */
	public String gzbhSelect(Parameters parameters, Bundle bundle) throws Exception {
		String query_gzbh = parameters.getString("query");
		LOGGER.info("query_gzbh " + query_gzbh);
		if (StringUtils.isNotBlank(query_gzbh)) {
			query_gzbh = "%" + query_gzbh + "%";
			// 查询工装编号
			Dataset datasetGz = Sys.query("mm_gzb", " gzid, gzbh ", " gzbh like ? ", null, new Object[] { query_gzbh });
			List<Map<String, Object>> list = datasetGz.getList();
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("label", list.get(i).get("gzbh"));
				list.get(i).put("value", list.get(i).get("gzbh"));
				list.get(i).put("title", list.get(i).get("gzbh"));
			}		
			bundle.put("list", list);
		}
		return "json:list";
	}
}
