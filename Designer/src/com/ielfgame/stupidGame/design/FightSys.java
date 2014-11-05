package com.ielfgame.stupidGame.design;

public class FightSys {
	/**************************************************************
	 * 
	 * ����, ����
	 * 
	 * buff //�����������ɻغϻ�����Զ 1.roundCount// 2.onEnter// 3.onExit//
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
	 * ÿ��Ӣ��һ��Id Ӣ����ֵ + Skill + Buff private: updateHeroSkill updateHeroValue
	 * onDie
	 * 
	 * 
	 * public: getCurrentSkill onRecevieBuff onBooldChange
	 * 
	 * ͳһ�ӿ�...onXXX,
	 * 
	 * FightManager --- Hero---�Լ�id, ����id, ���� Skill---�Լ�id, ������id
	 * 
	 * Buff ���� , ����, ���� getPositive?
	 * 
	 * onBuffEnter onBuffExit 1. ���� 1 2. �غ�ǰ n 3. �غϺ� n 4.
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
	 * //1. ����--> mustHit ? mustMiss? ���� ? //2. �˺�����, �˺�ֵ --> ���� �˺�����, �˺�ֵ? ����
	 * ���˺�����, ���˺�ֵ ?(����ֵ�) //3. ����--> ��û�з���? ��������,����ֵ? ���� ���˺�����, ���˺�ֵ ?(����ֵ�) //
	 * A computeHurtType, computeHurtValue // B computeBeHurt
	 * 
	 * //4. ʩ��buff--> ������ʩ��buff ? ��������ʩ��buff ? //5. onMoveExit
	 * 
	 * onRoundEnd
	 * 
	 * onFightEnd
	 * 
	 * 
	 * 1.��ֵģ�� 1.1 ս����ֵ 1.2 ս����ֵ(��) 1.3 Ӣ����ֵ 1.4 ������ֵ 1.5 կ����ֵ 1.6 ������ֵ
	 * 
	 * 
	 * 2.ͼ��ģ�� 1. hero stay attack beAttack // ��˸ʱ��, ��˸��ɫ // die
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
	 * ŭ������
	 * Ӣ�´�� 
	 * 		�Ե�һĿ�����170%�������������˺�
	 * �Ͽմ�� 
	 * 		�Ե�һĿ�����165%�������������˺�������Ŀ������30�㣬�������غ�
	 * ��������֮�� 
	 * 		�Ե�һĿ�����175%�������������˺���֮��2�غϼ�10%�ٶ� 
	 * ��ʹ֮��
	 * 		�Ե�һĿ�����200%�������������˺�������10%Ŀ����������1�غ� 
	 * �����ͻ� 
	 * 		�Ե�һĿ�����180%�������������˺�������Ŀ��2�غ� 
	 * ����ȭ
	 * 		������öԷ�2Ŀ�꣬���110%���������� 
	 * ������� 
	 * 		����150%�������������˺�����������20%�������ʣ�����2�غ� 
	 * �ѵ���ŭ��
	 * 		��һ����Ŀ�����220%�������������˺���//��������10%����������������2�غ� 
	 * ����б�
	 * 		��һ����Ŀ�����220%�������������˺�����������10%����������������2�غ� 
	 * ��ղ��� 
	 * 		�������������˺�������2�غ� 
	 * �����̭
	 * 		������Ѫ���ȵ���30%�ҶԷ���Ӣ��Ѫ���ȸ����Լ�ʱ����Է�Ѫ������ߵ�Ӣ�۽���Ѫ���� 
	 * ������ȡ 
	 * 		͵ȡһ���Է�Ӣ�۵ļ��ܣ�����2�غ� 
	 * ˪��ն
	 * 		��һ����Ŀ�����170%�������������˺���50%���ʱ���Ŀ�꣬����2�غ� 
	 * Ѫ�ȳ���
	 * 		�Ե�һĿ�����180%�������������˺���������˺���50%�ظ�����Ѫ�� 
	 * ���͹��� 
	 * 		�Ե�һĿ�����200%�������������˺�������1*�������˺�
	 * 
	 * �������ݵĹ������ܣ�rank2
	 * 
	 * ��������
	 * 
	 * �ط��ɵ� 
	 * 		����120%�������������˺� 
	 * ���� 
	 * 		����115%�������������˺���60%����Ŀ�꣬����2�غ�
	 * ����
	 * 		����120%�������������˺���50%�ض�����Ŀ�꣬����2�غ� 
	 * ��ä���� 
	 * 		����130%�������������˺���80%����ʹĿ�����н���20% 
	 * ������
	 * 		����һ������������ʼ����100%�������������˺�����Ծ���Σ��˺�10%�ݼ�
	 * �ҿշ籩
	 * 		������öԷ�3Ŀ�꣬�����80%������������30%�������Ŀ�꣬����һ�غ� 
	 * ����籩
	 * 		������öԷ�3Ŀ�꣬�����80%������������20%����Ŀ�꣬����һ�غ� 
	 * Ī���ȵ�����
	 * 		������öԷ�3Ŀ�꣬�����90%������������25%ʹĿ���ж�������һ�غ� 
	 * ��������� 
	 * 		��һ����Ŀ�����100%�������������˺�
	 * ˪������
	 * 		����140%�������������˺���50%���ʱ���Ŀ�꣬����1�غ� 
	 * �¿���֮�� 
	 * 		�Ե�һĿ�����130%�������������˺���������˺���30%�ָ�����Ѫ��
	 * ���� 
	 * 		����150%�������������˺���40%���ʽ���Ŀ��30%����������������2�غ� 
	 * �����ɵ�
	 * 		�Ե�һĿ�����150%ħ�����������˺�������1*�������˺� 
	 * ˹���ŭ�� 
	 * 		�������м���Ӣ�۸�50��ŭ��
	 * �����͸�
	 * 		������ü���2Ŀ�꣬�ظ�20%������������Ѫ�� 
	 * ������˹֮�� 
	 * 		������öԷ�2Ŀ�꣬ʹ���˯������һ�غ� 
	 * �׽�֮ŭ
	 * 		������ü���2Ŀ�꣬������50%����������������2�غ� 
	 * �ط������� 
	 * 		����60%�ܵ��ķ��������˺� 
	 * ������ף�� 
	 * 		��������20%������
	 * ��Ϣ֮�� 
	 * 		�Ե�һĿ�����150%�������������˺���50%���ʸ���200%�������������˺�
	 * ս����� 
	 * 		����һλ����Ӣ��100��ŭ��
	 * ���� 
	 * 		ǿ��һ��Ŀ�깥���Լ������Լ��������� ���Ƥ�� ����60%�ܵ������������˺�
	 * �����Ȼ�
	 * 		ʹ��һĿ�걻�Ȼ�����һ�غ� 
	 * ��Ĭ 
	 * 		������öԷ�2Ŀ�꣬ʹ�䲻��ʹ�ü��ܣ�����2�غ�
	 */
}