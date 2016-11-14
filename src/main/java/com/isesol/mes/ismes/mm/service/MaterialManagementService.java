package com.isesol.mes.ismes.mm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.isesol.ismes.platform.core.service.bean.Dataset;
import com.isesol.ismes.platform.module.Bundle;
import com.isesol.ismes.platform.module.Parameters;
import com.isesol.ismes.platform.module.Sys;

public class MaterialManagementService {
	
	private Logger log4j = Logger.getLogger(MaterialManagementService.class);

	/**
	 * 对外暴露的service
	 * 单个 wlid
	 * 多个 List<Map<String,Object>> list = parameters.get("wlids");
	 * 
	 * 根据物料编号查询物料信息
	 */
	public void materielInfoByWlid(Parameters parameters,Bundle bundle){
		String wlid = parameters.getString("wlid");
		List<Map<String,Object>> list = (List<Map<String, Object>>) parameters.get("wlids");
		if(StringUtils.isBlank(wlid) && CollectionUtils.isEmpty(list)){
			log4j.info("查询物料信息，id不能为空");
			bundle.put("materielInfo",  new HashMap<String,Object>());
			return;
		}
		//物料类别
		if(StringUtils.isNotBlank(wlid)){
			Dataset set = Sys.query("mm_wlb", "wlid,"/*物料id*/+"wlbh,"/*物料编号*/
					+"wlmc,"/*物料名称*/+"wldwdm,"/*物料单位代码*/+"wllxdm,"/*物料类型id*/
					+"wllbdm,"/*物料类别id*/+"wlzt,"/*物料状态*/+"wlcb,"/*物料成本*/
					+"wltm,"/*物料条码*/+"wlgg,"/*物料规格*/+"djsm,sysm,"/*刀具寿命*/ + "jgztdm", /*加工状态代码*/
					"wlid = ?"  , null, null,wlid);
			
			Map<String,Object> map = set.getMap();
			if(MapUtils.isNotEmpty(map)){
				String wldwdm = map.get("wldwdm").toString();
				String wldwmc = "";
				if("10".equals(wldwdm)){
					wldwmc = "件（个）";
				}
				if("20".equals(wldwdm)){
					wldwmc = "米";				
				}
				if("30".equals(wldwdm)){
					wldwmc = "公斤";		
				}
				if("40".equals(wldwdm)){
					wldwmc = "吨";	
				}
				map.put("wldwmc", wldwmc);
			}
			bundle.put("materielInfo",  map);
		}
		if(CollectionUtils.isNotEmpty(list)){
			StringBuffer sb = new StringBuffer("'");
			for(Map<String,Object> map : list){
				if(map.get("wlid") == null || StringUtils.isBlank( map.get("wlid").toString())){
					continue;
				}
				sb = sb.append(map.get("wlid").toString()).append("','");
			}
			if(sb.toString().endsWith(",'")){
				sb = sb.delete(sb.length() - 2, sb.length());
				Dataset set = Sys.query("mm_wlb", "wlid,"/*物料id*/+"wlbh,"/*物料编号*/
						+"wlmc,"/*物料名称*/+"wldwdm,"/*物料单位代码*/+"wllxdm,"/*物料类型id*/
						+"wllbdm,"/*物料类别id*/+"wlzt,"/*物料状态*/+"wlcb,"/*物料成本*/
						+"wltm,"/*物料条码*/+"wlgg,"/*物料规格*/+"djsm,sysm", /*刀具寿命*/
						"wlid in (" + sb + ")" , null, null);
				bundle.put("materielInfoList",  set.getList());
			}
		}
		
		
	}
	
	/**根据物料ID、编号、名称、类别查询物料信息
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlxx_new(Parameters parameters, Bundle bundle) {
		String param = " 1=1 ";
		if(null!=parameters.getString("val_wl")&&!"".equals(parameters.getString("val_wl"))){
			param+=" and wlid in "+parameters.getString("val_wl");
		}
		if(null!=parameters.getString("wlbh")&&!"".equals(parameters.getString("wlbh"))){
			param+=" and wlbh = '"+parameters.getString("wlbh")+"'";
		}
		if(null!=parameters.getString("wlmc")&&!"".equals(parameters.getString("wlmc"))){
			param+=" and wlmc like '%"+parameters.getString("wlmc")+"%'";
		}
		if(null!=parameters.getString("wllb")&&!"".equals(parameters.getString("wllb"))){
			param+=" and wllbdm = '"+parameters.getString("wllb")+"'";
		}
		if(null!=parameters.getString("val_tm")&&!"".equals(parameters.getString("val_tm"))){
			param+=" and wltm in "+parameters.getString("val_tm");
		}
		Dataset dataset_wlxx = Sys.query("mm_wlb"," aqkc,wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz,sysm ",param, null, new Object[]{});
		bundle.put("wlxx",  dataset_wlxx.getList());
	}
	public void query_wlxx(Parameters parameters, Bundle bundle) {
		Dataset dataset_wlxx = Sys.query("mm_wlb","aqkc,wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz", "wlid in "+parameters.get("val_wl").toString(), null, new Object[]{});
		bundle.put("wlxx",  dataset_wlxx.getList());
	}
	/**根据物料ID查询物料信息
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlxx_by_wlids(Parameters parameters, Bundle bundle) {
		String param = " 1=1 ";
		if(null!=parameters.getString("val_wl")&&!"".equals(parameters.getString("val_wl"))){
			param+=" and wlid in "+parameters.getString("val_wl");
		}
		Dataset dataset_wlxx = Sys.query("mm_wlb","aqkc,wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz",param, null, new Object[]{});
		bundle.put("wlxx",  dataset_wlxx.getList());
	}
	public void query_wlxxBybhmc(Parameters parameters, Bundle bundle) {
		String query_wlbh = parameters.getString("query_wlbh"); //  物料编号
		String query_wlmc = parameters.getString("query_wlmc"); //  物料名称
		String con = "1 = 1 ";
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
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz", con , null, val.toArray());
		bundle.put("wlxx",  dataset_wlxx.getList());
	}
	
	/**
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlxxByWlbh(Parameters parameters, Bundle bundle) {
		String wlbh = parameters.getString("wlbh");
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz", "wlbh = ?", null, new Object[]{wlbh});
		bundle.put("wlxx",  dataset_wlxx.getList());
	}
	
	/**查询工装ID
	 * @param parameters
	 * @param bundle
	 */
	public void queryGzxx(Parameters parameters, Bundle bundle) {
		String gzbh = parameters.getString("gzbh");
		Dataset dataset_gzxx = Sys.query("mm_gzb","gzid,gzbh,gzlxbh", "gzbh =? ", null, new Object[]{gzbh});
		bundle.put("gzxx",  dataset_gzxx.getList());
	}
	/**查询物料信息
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlxxkc(Parameters parameters, Bundle bundle) {
		
		String query_wlbh = parameters.getString("query_wlbh"); //  物料编号
		String query_wlmc = parameters.getString("query_wlmc"); //  物料名称
		String query_wllxdm = parameters.getString("query_wllxdm"); // 物料类型代码
		String query_wllxdm_ids = parameters.getString("query_wllxdm_ids");
		
		String query_wllbdm = parameters.getString("query_wllbdm"); // 物料类别代码 
		String query_wllbdm_ids = parameters.getString("query_wllbdm_ids");
		String query_wlzt = parameters.getString("query_wlzt"); 
		
		String query_wlgg = parameters.getString("query_wlgg"); // 物料规格
		String query_wlgg_eq = parameters.getString("query_wlgg_eq"); // 物料规格
		
		String con = "1 = 1 ";
		List<Object> val = new ArrayList<Object>();
		if(StringUtils.isNotBlank(query_wlbh)) 
		{
			con = con + " and ( wlbh like ? or wlbh like ? or wlbh like ? ) ";
			val.add("%"+query_wlbh.toLowerCase()+"%");
			val.add("%"+query_wlbh.toUpperCase()+"%");
			val.add("%"+query_wlbh+"%");
		}
		if(StringUtils.isNotBlank(query_wlmc)) 
		{
			con = con + " and wlmc like ? ";
			val.add("%"+query_wlmc+"%");
		}
		if(StringUtils.isNotBlank(query_wllxdm))
		{
			con = con + " and wllxdm = ? ";
			val.add(query_wllxdm);
		}
		if(StringUtils.isNotBlank(query_wllxdm_ids))
		{
			con = con + " and wllxdm in ( " + query_wllxdm_ids + " ) ";
		}
		if(StringUtils.isNotBlank(query_wllbdm))
		{
			con = con + " and wllbdm = ? ";
			val.add(query_wllbdm);
		}
		if(StringUtils.isNotBlank(query_wllbdm_ids))
		{
			con = con + " and wllbdm in ( " + query_wllbdm_ids + " ) ";
		}
		if(StringUtils.isNotBlank(query_wlzt))
		{
			con = con + " and wlzt = ? ";
			val.add(query_wlzt);
		}
		if(StringUtils.isNotBlank(query_wlgg))
		{
			con = con + " and wlgg like ? ";
			val.add("%"+query_wlgg+"%");
		}
		if(StringUtils.isNotBlank(query_wlgg_eq))
		{
			con = con + " and wlgg = ? ";
			val.add(query_wlgg_eq);
		}
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz,jgztdm,djsm,sysm,wlpp,wltm", con, null, "wlid,wlgg", val.toArray());
		bundle.put("wlxx",  dataset_wlxx.getList());
		bundle.put("wlxxMap",  dataset_wlxx.getMap());
	}
	
	/**
	 * 新建一个
	 * @param parameters
	 * @param bundle
	 */
	public void insertNewWlInfo(Parameters parameters, Bundle bundle){
		Map<String,Object> hashmap = (Map<String, Object>) parameters.get("wlxx");
		if(MapUtils.isEmpty(hashmap)){
			log4j.info("新建一个物料信息对象，map不能为空");
			return;
		}
		if(hashmap.get("wldwdm") == null){
			hashmap.put("wldwdm", "0");
		}
		try{
			Sys.insert("mm_wlb", hashmap);
		}catch(Exception e){
			e.printStackTrace();
		}
		bundle.put("wlid", hashmap.get("wlid"));
	}
	
	/**
	 * 修改一个
	 * @param parameters
	 * @param bundle
	 */
	public void updateWlInfo(Parameters parameters, Bundle bundle){
		Map<String,Object> hashmap = (Map<String, Object>) parameters.get("wlxx");
		String condition = parameters.getString("condition");
		List<Object> conditionValues = (List<Object>) parameters.get("conditionValues");
		if(MapUtils.isEmpty(hashmap)){
			log4j.info("修改一个物料信息对象，map不能为空");
			return;
		}
		try{
			Sys.update("mm_wlb", hashmap,condition,conditionValues.toArray());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除一个
	 * @param parameters
	 * @param bundle
	 */
	public void deleteWlInfoById(Parameters parameters, Bundle bundle){
		if(parameters.get("wlid") == null || StringUtils.isBlank(parameters.get("wlid").toString())){
			log4j.info("删除物料，id不能为空");
			return;
		}
		String wlid =  parameters.get("wlid").toString();
		Sys.delete("mm_wlb", " wlid = ? ", new Object[]{wlid});
	}
	
	/**
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlxxByWlid(Parameters parameters, Bundle bundle) {
		String wlid = parameters.getString("wlid");
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz,djsm", "wlid = ?", null, new Object[]{wlid});
		bundle.put("wlxx",  dataset_wlxx.getList());
	}

	/**根据毛坯规格查询物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public void query_wlxx_by_mpgg(Parameters parameters, Bundle bundle) {
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlgg,jgztdm,wlid", "wlgg like '%"+parameters.get("query").toString()+"%'"+" and wllbdm in ('40','50')", null, new Object[]{});
		bundle.put("wlxx", dataset_wlxx.getList());
	}
	/**根据物料id查询物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public void query_wlxx_by_wlid(Parameters parameters, Bundle bundle) {
		Dataset dataset_wlxx;
		if(!"".equals(parameters.get("wlid"))){
		dataset_wlxx = Sys.query("mm_wlb","wlgg,wlid", "wlid in("+parameters.get("wlid").toString()+") and wllbdm in ('40','50')", null, new Object[]{});
		}else{
		dataset_wlxx = Sys.query("mm_wlb","wlgg,wlid", " wllbdm in ('40','50')", null, new Object[]{});
		}
		bundle.put("wlxx", dataset_wlxx.getList());
	}
	/**根据物料ID 查询 物料类别为：量具、夹具
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public void query_wlxx_by_wlid_where_wllb(Parameters parameters, Bundle bundle) {
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz,jgztdm,djsm,wlpp,wltm", " wllbdm in ('20','30')", null, new Object[]{});
		bundle.put("wlxx", dataset_wlxx.getList());
	}
	
	/**根据工装编号 查询 物料信息
	 * @param parameters
	 * @param bundle
	 * @return
	 */
	public void query_wlxx_by_gzbh(Parameters parameters, Bundle bundle) {
		String param = " 1=1 ";
		if(null!=parameters.getString("val_gz")&&!"".equals(parameters.getString("val_gz"))){
			param+=" and wlbh in "+parameters.getString("val_gz");
		}
		Dataset dataset_wlxx = Sys.query("mm_wlb"," wldwdm, wllbdm, wlpp, wlgg, wlcb, aqkc, jgztdm, wlid, wlbh, djsm, yxq, sysm, wlzt, bz, wltm, wlmc, wllxdm ", param, null, new Object[]{});
		bundle.put("wlxx", dataset_wlxx.getList());
	}
	
	public void query_wlxx_bywlgg(Parameters parameters, Bundle bundle){
		
		String wlgg = parameters.getString("wlggs");
		String wllb = parameters.getString("wllb");
		
		String wllbcon = "";
		if(wllb != null && !"".equals(wllb)){
			wllbcon = " and wllbdm = '" +wllb+"'";
		}
		
		Dataset dataset_wlxx = Sys.query("mm_wlb","wltm,wlid,wlgg,wldwdm", "wlgg in " + wlgg + wllbcon, null, new Object[]{});
		
		bundle.put("wlxxs", dataset_wlxx.getList());
	}
	/**
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlxxByWlbh_new(Parameters parameters, Bundle bundle) {
//		String wlbh = parameters.getString("wlbh");
		String param = " 1=1 ";
		if(null!=parameters.getString("wlbh")&&!"".equals(parameters.getString("wlbh"))){
			param+=" and wlbh = '"+parameters.getString("wlbh")+"'";
		}
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlbh,wlmc,wldwdm,wllxdm,wllbdm,wlzt,wlcb,wltm,wlgg,bz", param, null, new Object[]{});
		bundle.put("wlxx",  dataset_wlxx.getList());
	}
	/**
	 * @param parameters
	 * @param bundle
	 */
	public void query_wlggid_bywlggxx(Parameters parameters, Bundle bundle) {
		
		String param = " 1=1 and wlgg = ?";
		Dataset dataset_wlxx = Sys.query("mm_wlb","wlid,wlgg", param, null, new Object[]{parameters.getString("wlid")});
		if (null != dataset_wlxx && dataset_wlxx.getList().size() > 0){
			bundle.put("wlgg",  dataset_wlxx.getList());
		}else{
			param = " 1=1 and wlid = ?";
			dataset_wlxx = Sys.query("mm_wlb","wlid,wlgg", param, null, new Object[]{parameters.getString("wlid")});
			bundle.put("wlgg",  dataset_wlxx.getList());
		}
	}
	
}
