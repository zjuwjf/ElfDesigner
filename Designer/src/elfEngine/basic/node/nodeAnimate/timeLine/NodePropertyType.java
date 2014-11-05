package elfEngine.basic.node.nodeAnimate.timeLine;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.INodePropertyChange;

public enum NodePropertyType {
	PositionType("_Position"), RotateType("_Rotate"), ScaleType("_Scale"), ColorType("_Color"), VisibleType("_Visible");
	private final String mString;

	NodePropertyType(String string) {
		mString = string;
	}

	public String toString() {
		return mString;
	}
	
	public void modifier(final ElfMotionKeyNode prev, final ElfMotionKeyNode next, final ElfNode node, int progress) {
		final INodePropertyChange iNodePropertyChange = node.getNodePropertyChange();

		node.setNodePropertyChange(null);
		
		if(this == VisibleType) { 
			if(progress >= next.getTime()) { 
				node.setVisible(next.getVisible()); 
				node.setNodePropertyChange(iNodePropertyChange); 
				return ;
			} 
		} 
		
		if(next.getTime() == prev.getTime()) { 
			switch(this) { 
			case ColorType:
				node.setColor(prev.getColor());
				break;
			case PositionType:
				node.setPosition(prev.getPosition());
				break;
			case RotateType:
				node.setRotate(prev.getRotate());
				break;
			case ScaleType:
				node.setScale(prev.getScale());
				break;
			case VisibleType:
				node.setVisible(prev.getVisible());
				break;
			} 
			
			node.setNodePropertyChange(iNodePropertyChange);
			return ;
		}
		
		if(progress > next.getTime()) {
			switch(this) { 
			case ColorType:
				node.setColor(next.getColor());
				break; 
			case PositionType:
				node.setPosition(next.getPosition());
				break;
			case RotateType:
				node.setRotate(next.getRotate());
				break;
			case ScaleType:
				node.setScale(next.getScale());
				break;
			case VisibleType:
				node.setVisible(next.getVisible());
				break;
			} 
			
			node.setNodePropertyChange(iNodePropertyChange);
			return ;
		} else if(progress < prev.getTime()) {
			switch(this) { 
			case ColorType:
				node.setColor(prev.getColor());
				break;
			case PositionType:
				node.setPosition(prev.getPosition());
				break;
			case RotateType:
				node.setRotate(prev.getRotate());
				break;
			case ScaleType:
				node.setScale(prev.getScale());
				break;
			case VisibleType:
				node.setVisible(prev.getVisible());
				break;
			} 
			
			node.setNodePropertyChange(iNodePropertyChange);
			return ;
		} 
		
		final float rate = (progress-prev.getTime())/((float)next.getTime()-prev.getTime());
		final float r = prev.getInterType().getInterpolation(rate);
		
		switch(this) {
		case ColorType:
			node.setColor(ElfColor.between(prev.getColor(), next.getColor(), r));
			break;
		case PositionType:
			node.setPosition(ElfPointf.between(prev.getPosition(), next.getPosition(), r));
			break;
		case RotateType:
			node.setRotate(between(prev.getRotate(), next.getRotate(), r));
			break;
		case ScaleType:
			node.setScale(ElfPointf.between(prev.getScale(), next.getScale(), r));
			break;
		case VisibleType:
			node.setVisible(prev.getVisible());
			break;
		}
		
		node.setNodePropertyChange(iNodePropertyChange);
	}
	
	static float between(float a, float b, float r) {
		return a*(1-r) + b*r;
	}
}
