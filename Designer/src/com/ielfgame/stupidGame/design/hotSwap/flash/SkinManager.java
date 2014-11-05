package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.HashMap;

import com.ielfgame.stupidGame.data.StringHelper;

public class SkinManager {
	private final static HashMap<String, String> sDefaultSkinMap = new HashMap<String, String>();
	
	static {
		// ['腿左.png'] = '%stz.png',
		// ['腿右.png'] = '%sty.png',
		// ['手左.png'] = '%ssz.png',
		// ['手右.png'] = '%ssy.png',
		// ['身前.png'] = '%ssq.png',
		// ['身后.png'] = '%ssh.png',
		// ['头.png'] = '%st.png',
		// ['武器.png'] = '%swq.png',
		// ['盾.png'] = '%sd.png',
		// ['武器2.png'] = '%swq2.png',
		// ['单手斧.png'] = '%swq.png'
		
		sDefaultSkinMap.put("腿左.png", "skin_1tz.png");
		sDefaultSkinMap.put("腿右.png", "skin_1ty.png");
		
		sDefaultSkinMap.put("手左.png", "skin_1sz.png");
		sDefaultSkinMap.put("手右.png", "skin_1sy.png");
		
		sDefaultSkinMap.put("身前.png", "skin_1sq.png");
		sDefaultSkinMap.put("身后.png", "skin_1sh.png");
		
		sDefaultSkinMap.put("尾巴.png", "skin_1wb.png");
		sDefaultSkinMap.put("尾.png", "skin_1wb.png");
		
		sDefaultSkinMap.put("头.png", "skin_1t.png");
		sDefaultSkinMap.put("头后.png", "skin_1th.png");
		sDefaultSkinMap.put("头发.png", "skin_1tf.png");
		
		sDefaultSkinMap.put("帽子.png", "skin_1mz.png");
		sDefaultSkinMap.put("帽.png", "skin_1mz.png");
		
		sDefaultSkinMap.put("装饰.png", "skin_1zs.png");
		
		sDefaultSkinMap.put("披风.png", "skin_1pf.png");
		sDefaultSkinMap.put("翅膀.png", "skin_1cb.png");
		
		sDefaultSkinMap.put("武器.png", "skin_1wq.png");
		
		sDefaultSkinMap.put("武器左.png", "skin_1wqz.png");
		sDefaultSkinMap.put("武器右.png", "skin_1wqy.png");
		
		sDefaultSkinMap.put("匕首.png", "skin_1bishou.png");
		sDefaultSkinMap.put("匕首2.png", "skin_1bishouer.png");
		
		sDefaultSkinMap.put("步枪.png", "skin_1buqiang.png");
		sDefaultSkinMap.put("铳枪.png", "skin_1tongqiang.png");
		
		sDefaultSkinMap.put("单手斧.png", "skin_1danshoufu.png");
		sDefaultSkinMap.put("单手斧2.png", "skin_1danshoufuer.png");
		
		sDefaultSkinMap.put("单手剑.png", "skin_1danshoujian.png");
		sDefaultSkinMap.put("盾.png", "skin_1dun.png");
		
		sDefaultSkinMap.put("法杖.png", "skin_1fazhang.png");
		sDefaultSkinMap.put("飞镖.png", "skin_1feibiao.png");
		
		sDefaultSkinMap.put("弓.png", "skin_1gong.png");
		
		sDefaultSkinMap.put("箭只.png", "skin_1jianzhi.png");
		
		sDefaultSkinMap.put("魔杖.png", "skin_1mozhang.png");
		
		sDefaultSkinMap.put("枪.png", "skin_1qiang.png");
		
		sDefaultSkinMap.put("手枪.png", "skin_1shouqiang.png");
		
		sDefaultSkinMap.put("双手剑.png", "skin_1shuangshoujian.png");
		
		sDefaultSkinMap.put("霰弹枪.png", "skin_1shandanqiang.png");
		
	}
	
	private static final String getSkinFromDefault(final String resid) {
		if(resid == null) {
			return null;
		}
		
		String ret = sDefaultSkinMap.get(resid);
		if(ret == null) {
			ret = resid;
		}
		return ret;
	}
	
	public static String getSkinById(final String resid, final int id) {
		final String myresid = getSkinFromDefault(resid);
		/**
		 * 
		 */
		if(myresid != null && myresid.startsWith("skin_")) {
			final String ret = StringHelper.replaceFirstInt(myresid, Math.abs(id));
			if(ret != null) {
				return ret;
			} else {
				System.err.println("Replace Skin:"+resid+" failed!");
			}
		}
		
		return resid;
	}
}
