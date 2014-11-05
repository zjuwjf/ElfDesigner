package com.ielfgame.stupidGame.design;

public class FightSys {
	/**************************************************************
	 * 
	 * 主动, 被动
	 * 
	 * buff //立即或者若干回合或者永远 1.roundCount// 2.onEnter// 3.onExit//
	 * 
	 * 4.onEffect//on
	 * 
	 * skill
	 * 
	 * onPickObjects onMoveEnter ... onMoveExit onAttack ... onBeAttack
	 * onRoundStart ... onRoundEnd
	 * 
	 * hero
	 * 
	 * 每个英雄一个Id 英雄数值 + Skill + Buff private: updateHeroSkill updateHeroValue
	 * onDie
	 * 
	 * 
	 * public: getCurrentSkill onRecevieBuff onBooldChange
	 * 
	 * 统一接口...onXXX,
	 * 
	 * FightManager --- Hero---自己id, 技能id, 行列 Skill---自己id, 所有者id
	 * 
	 * Buff 种类 , 负面, 正面 getPositive?
	 * 
	 * onBuffEnter onBuffExit 1. 立即 1 2. 回合前 n 3. 回合后 n 4.
	 * 
	 * onFightStart
	 * 
	 * onRoundStart
	 * 
	 * //every hero //Make Skill doIt() isAttack?
	 * 
	 * onStartAttack ?
	 * 
	 * onPickObjects onMoveEnter ... onMoveExit
	 * 
	 * for each Objects hit? miss?
	 * 
	 * hurt? A : computeHurt---> hurtType, hurtValue B : computeBehurt---> B :
	 * computeRebound---> hurtType, hurtValue A : computeBehurt--->
	 * 
	 * buff? A : putBuffOnAttack B : putBuffOnBeActtack
	 * 
	 * onMoveExit
	 * 
	 * //Make Value
	 * 
	 * 
	 * onPickAttacker onPickSkill // updateSkill
	 * 
	 * onPickObjects onPickLocal, onPickInter, onPickTime onMoveEnter onAttack
	 * //1. 命中--> mustHit ? mustMiss? 计算 ? //2. 伤害类型, 伤害值 --> 计算 伤害类型, 伤害值? 计算
	 * 被伤害类型, 被伤害值 ?(额外抵挡) //3. 反伤--> 有没有反伤? 反伤类型,反伤值? 计算 被伤害类型, 被伤害值 ?(额外抵挡) //
	 * A computeHurtType, computeHurtValue // B computeBeHurt
	 * 
	 * //4. 施放buff--> 攻击者施放buff ? 被攻击者施放buff ? //5. onMoveExit
	 * 
	 * onRoundEnd
	 * 
	 * onFightEnd
	 * 
	 * 
	 * 1.数值模型 1.1 战斗数值 1.2 战场数值(阵法) 1.3 英雄数值 1.4 技能数值 1.5 寨主数值 1.6 道具数值
	 * 
	 * 
	 * 2.图形模型 1. hero stay attack beAttack // 闪烁时间, 闪烁颜色 // die
	 * 
	 * 2. skill onAttackStart onAttak
	 * 
	 * onBeAttack
	 * 
	 * 3. buff //Animate
	 * 
	 * 4. label, number
	 * 
	 * 5. black
	 * 
	 * 6. icons
	 * 
	 * hero action > skill action bindSkill --> readySkill --> attackSkill -->
	 * 
	 * readyValue --> //HP, MP
	 * 
	 * 1. onIsToAttack 2. onPickSkill
	 * 
	 * 3. onAttackStart 4. onPickObjects 5. onAttackMoveIn 6. onAttack ----> 7.
	 * onMustHit? 8. onMustMiss ? ... 9. onComputeHit ? ... 10. onHit ? 11.
	 * onBeAttack 12. onRebound 13. onPutBuff 14. onBePutBuff 15. onMiss ? 16.
	 * onAttackMoveOut 17. onAttackEnd
	 * 
	 * skill 3. onAttackStart (self, manage) 4. onPickObjects (self, manage) 5.
	 * onAttackMoveIn (self, picks, manage)//( position, time, inter ) for each
	 * picks //attacker, beAttacker 6. onAttack (self, pick, manage, int [] ret)
	 * 7. onMustHit(self, pick, manage) 8. onMustMiss(self, pick, manage) 9.
	 * onComputeHit(self, pick, manage) 10. onHit(self, pick, manage) 11.
	 * onBeAttack(self, pick, manage, int [] hurt) 12 onHurt(pick, int [] hurt)
	 * 12. onRebound(self, pick, int [] hurt) 13. onPutBuff 14. onBePutBuff 15.
	 * onMiss ? 16. onAttackMoveOut 17. onAttackEnd
	 * 
	 * buff 1. positive, negative 2. onInstance, onRoundStart, onRoundEnd 3.
	 * roundCount 4.
	 */

	/***
	 * 怒气技能
	 * 英勇打击 
	 * 		对单一目标造成170%物理攻击力的伤害
	 * 断空打击 
	 * 		对单一目标造成165%物理攻击力的伤害，降低目标命中30点，持续两回合
	 * 塔里托林之击 
	 * 		对单一目标造成175%物理攻击力的伤害，之后2回合加10%速度 
	 * 天使之剑
	 * 		对单一目标造成200%物理攻击力的伤害，削弱10%目标物理护甲1回合 
	 * 烈焰猛击 
	 * 		对单一目标造成180%物理攻击力的伤害，灼伤目标2回合 
	 * 百裂拳
	 * 		随机作用对方2目标，造成110%物理攻击力 
	 * 暴力碎击 
	 * 		产生150%物理攻击力的伤害，增加自身20%暴击概率，持续2回合 
	 * 裂地者怒击
	 * 		对一横排目标造成220%物理攻击力的伤害，//增加自身10%物理攻击力，持续2回合 
	 * 玄武残暴
	 * 		对一纵排目标造成220%物理攻击力的伤害，增加自身10%物理攻击力，持续2回合 
	 * 金刚不坏 
	 * 		自身免疫所有伤害，持续2回合 
	 * 灵魂淘汰
	 * 		当自身血量比低于30%且对方有英雄血量比高于自己时，与对方血量比最高的英雄交换血量比 
	 * 法术窃取 
	 * 		偷取一个对方英雄的技能，持续2回合 
	 * 霜冻斩
	 * 		对一横排目标造成170%物理攻击力的伤害，50%几率冰冻目标，持续2回合 
	 * 血腥偿还
	 * 		对单一目标造成180%物理攻击力的伤害，按造成伤害的50%回复自身血量 
	 * 狂猛攻击 
	 * 		对单一目标造成200%物理攻击力的伤害并附加1*力量的伤害
	 * 
	 * 另需横和纵的攻击技能：rank2
	 * 
	 * 法术技能
	 * 
	 * 秘法飞弹 
	 * 		产生120%法术攻击力的伤害 
	 * 黑焰 
	 * 		产生115%法术攻击力的伤害，60%灼伤目标，持续2回合
	 * 灼烧
	 * 		产生120%法术攻击力的伤害，50%重度灼伤目标，持续2回合 
	 * 致盲密能 
	 * 		产生130%法术攻击力的伤害，80%几率使目标命中降低20% 
	 * 闪电链
	 * 		产生一道闪电链，初始产生100%法术攻击力的伤害，跳跃两次，伤害10%递减
	 * 乱空风暴
	 * 		随机作用对方3目标，各造成80%法术攻击力，30%几率麻痹目标，持续一回合 
	 * 烈焰风暴
	 * 		随机作用对方3目标，各造成80%法术攻击力，20%灼伤目标，持续一回合 
	 * 莫甘娜的轻语
	 * 		随机作用对方3目标，各造成90%法术攻击力，25%使目标中毒，持续一回合 
	 * 超声冲击波 
	 * 		对一横排目标造成100%法术攻击力的伤害
	 * 霜冻攻击
	 * 		产生140%法术攻击力的伤害，50%几率冰冻目标，持续1回合 
	 * 德库拉之牙 
	 * 		对单一目标造成130%法术攻击力的伤害，按造成伤害的30%恢复自身血量
	 * 亡溺 
	 * 		产生150%法术攻击力的伤害，40%几率降低目标30%物理攻击力，持续2回合 
	 * 致死飞弹
	 * 		对单一目标造成150%魔法攻击力的伤害并附加1*智力的伤害 
	 * 斯库尔怒吼 
	 * 		增加所有己方英雄各50点怒气
	 * 生命赐福
	 * 		随机作用己方2目标，回复20%法术攻击力的血量 
	 * 提述勒斯之吟 
	 * 		随机作用对方2目标，使其昏睡，持续一回合 
	 * 白金之怒
	 * 		随机作用己方2目标，提升其50%物理攻击力，持续2回合 
	 * 秘法保护者 
	 * 		反弹60%受到的法术攻击伤害 
	 * 风舞者祝福 
	 * 		增加自身20%闪避率
	 * 窒息之刃 
	 * 		对单一目标造成150%物理攻击力的伤害，50%几率附加200%物理攻击力的伤害
	 * 战斗振奋 
	 * 		增加一位己方英雄100点怒气
	 * 嘲讽 
	 * 		强制一个目标攻击自己，在自己死亡后解除 尖刺皮肤 反弹60%受到的物理攻击伤害
	 * 心智魅惑
	 * 		使单一目标被魅惑，作用一回合 
	 * 沉默 
	 * 		随机作用对方2目标，使其不能使用技能，持续2回合
	 */
}
