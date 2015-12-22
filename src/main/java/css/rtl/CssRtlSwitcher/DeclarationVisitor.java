package css.rtl.CssRtlSwitcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.ECSSExpressionOperator;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.decl.shorthand.CSSShortHandDescriptor;
import com.helger.css.decl.shorthand.CSSShortHandRegistry;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.property.ECSSProperty;

public class DeclarationVisitor extends DefaultCSSVisitor {
	
	Logger log = Logger.getLogger(DeclarationVisitor.class.getName());

	@Override
	public void onDeclaration(CSSDeclaration dec) {
		// TODO Auto-generated method stub
		super.onDeclaration(dec);
		
		
		String propertyName=dec.getProperty();
		propertyName=propertyName.toLowerCase();
		//log.debug("declaration name:"+propertyName);
		
		
		//flip sides : right <--> left
		if(Mappings.LeftRight.contains(propertyName)){
			leftRightFlipper(dec);
		}
		
		//direction
		if(Mappings.Direction.contains(propertyName)){
			directionFlipper(dec);
		}

		//Quad flipper
		if(Mappings.Quad.containsKey(propertyName)){
			quadFlipper(dec);
		}
		
		//Radius Quad flipper
		if(Mappings.QuadRadius.contains(propertyName)){
			radiusQuadFlipper(dec);
		}
		
		//property name flipper
		if(Mappings.PropertyName.containsKey(propertyName)){
			propertyNameFlipper(dec);
		}
		
		//background position
		if(propertyName.equals("background-position")){
			backgroundPositionFlipper( dec);
		}
		
		//background position x
		if(propertyName.equals("background-position-x")){
			backgroundPositionFlipper( dec);
		}
		
		//background
		if(propertyName.equals("background")){
			backgroundFlipper( dec);
		}
		
		//box-shadow
		if(propertyName.equals("box-shadow")){
			shadowBoxFlipper( dec);
		}	
		
		
		
	}

	
	
private void leftRightFlipper(CSSDeclaration dec){
		
		CSSExpression expression=dec.getExpression();
		CSSExpressionMemberTermSimple member=(CSSExpressionMemberTermSimple)expression.getMemberAtIndex(0);
		String memberValue=member.getValue();
		if(memberValue.equalsIgnoreCase("left")){member.setValue("right");}else
		if(memberValue.equalsIgnoreCase("right")){member.setValue("left");}
			
	}
	
	private void directionFlipper(CSSDeclaration dec){
		
		CSSExpression expression=dec.getExpression();
		CSSExpressionMemberTermSimple member=(CSSExpressionMemberTermSimple)expression.getMemberAtIndex(0);
		String memberValue=member.getValue();
		if(memberValue.equalsIgnoreCase("ltr")){member.setValue("rtl");}else
		if(memberValue.equalsIgnoreCase("rtl")){member.setValue("ltr");}
			
	}

	private void quadFlipper(CSSDeclaration dec){
		
		String propertyName=dec.getProperty();
		propertyName=propertyName.toLowerCase();
		
		//get shorthand for the property
		ECSSProperty propertyShorthand=Mappings.Quad.get(propertyName);
		
		CSSShortHandDescriptor shortHand = CSSShortHandRegistry.getShortHandDescriptor (propertyShorthand);
		List <CSSDeclaration> splittedDecls = shortHand.getSplitIntoPieces (dec);
		
		
		//add default values for missing fields
		List<ICSSExpressionMember> newMembersList =new ArrayList<ICSSExpressionMember>();
		for(CSSDeclaration splitted:splittedDecls){
			
			CSSExpression tempExpression=splitted.getExpression();
			
			for(int i=0;i<tempExpression.getMemberCount();i++){			
				newMembersList.add(tempExpression.getMemberAtIndex(i));
			}
			
		}

		dec.getExpression().removeAllMembers();
		for(ICSSExpressionMember tempMember : newMembersList){
			dec.getExpression().addMember(tempMember);
		}
		//End add default values for missing fields
		
		
		
		
		//get left margin value
		String leftValue=null;
		for(CSSDeclaration splitted:splittedDecls){
			if(splitted.getProperty().equalsIgnoreCase(propertyName + "-left")){
				CSSExpressionMemberTermSimple m=(CSSExpressionMemberTermSimple)splitted.getExpression().getMemberAtIndex(0);
				leftValue=m.getValue();
			}
		}

		//get right margin value
		String rightValue=null;
		for(CSSDeclaration splitted:splittedDecls){
			if(splitted.getProperty().equalsIgnoreCase(propertyName+"-right")){
				CSSExpressionMemberTermSimple m=(CSSExpressionMemberTermSimple)splitted.getExpression().getMemberAtIndex(0);
				rightValue=m.getValue();
			}
		}
		
		//quad set margin left and right
		for(CSSDeclaration splitted:splittedDecls){
			if(splitted.getProperty().equalsIgnoreCase(propertyName+"-left")){
				CSSExpressionMemberTermSimple m=(CSSExpressionMemberTermSimple)splitted.getExpression().getMemberAtIndex(0);
				m.setValue(rightValue);
			}
			
			if(splitted.getProperty().equalsIgnoreCase(propertyName+"-right")){
				CSSExpressionMemberTermSimple m=(CSSExpressionMemberTermSimple)splitted.getExpression().getMemberAtIndex(0);
				m.setValue(leftValue);
			}
		}					
			
	}
	

	private void radiusQuadFlipper(CSSDeclaration dec){
		
		String propertyName=dec.getProperty();
		propertyName=propertyName.toLowerCase();
		
		
		
		List<ICSSExpressionMember> allMembers=dec.getExpression().getAllMembers();
		List<CSSExpressionMemberTermSimple> beforeOperator=new ArrayList<CSSExpressionMemberTermSimple>();
		List<CSSExpressionMemberTermSimple> afterOperator=new ArrayList<CSSExpressionMemberTermSimple>();
		
		boolean operatorFound=false;
		for(ICSSExpressionMember member:allMembers){
			if(member instanceof ECSSExpressionOperator){
				operatorFound=true;
				continue;
			}

			if(member instanceof CSSExpressionMemberTermSimple && !operatorFound){
				beforeOperator.add((CSSExpressionMemberTermSimple)member);
				continue;
			}
			
			if(member instanceof CSSExpressionMemberTermSimple && operatorFound){
				afterOperator.add((CSSExpressionMemberTermSimple)member);
			}
			
		}
		
		int membersCount=beforeOperator.size();
			
		//get shorthand for the property

		

		String topLeftValue=null;
		String topRightValue=null;
		String bottomRightValue=null;
		String bottomLeftValue=null;
		
		if(membersCount==0 || membersCount==1)return;
		
		if(membersCount==2 || membersCount==3 || membersCount==4){
			//get top left		
			CSSExpressionMemberTermSimple m0=beforeOperator.get(0);
			topLeftValue=m0.getValue();
		
			//get top right
			CSSExpressionMemberTermSimple m1=beforeOperator.get(1);
			topRightValue=m1.getValue();
		}
		
		if(membersCount==3 || membersCount==4){
			//get bottom right
			CSSExpressionMemberTermSimple m2=beforeOperator.get(2);
			bottomRightValue=m2.getValue();
		}
		
		if(membersCount==4){

			//get bottom left		
			CSSExpressionMemberTermSimple m3=beforeOperator.get(3);
			bottomLeftValue=m3.getValue();
		}
		
		
		//flip		
		if(membersCount==2 || membersCount==3 || membersCount==4){
			CSSExpressionMemberTermSimple m0=beforeOperator.get(0);
			m0.setValue(topRightValue);
			
			CSSExpressionMemberTermSimple m1=beforeOperator.get(1);
			m1.setValue(topLeftValue);
		}
		
		if(membersCount==3){
			CSSExpressionMemberTermSimple m2=beforeOperator.get(2);
			m2.setValue(topRightValue);
			
			dec.getExpression().addMember(new CSSExpressionMemberTermSimple(bottomRightValue));
		}
		
		
		if(membersCount==4){
			CSSExpressionMemberTermSimple m2=beforeOperator.get(2);
			m2.setValue(bottomLeftValue);
			
			CSSExpressionMemberTermSimple m3=beforeOperator.get(3);
			m3.setValue(bottomRightValue);
		}
		
		
		membersCount=afterOperator.size();
		if(membersCount==0 || membersCount==1)return;
		
		String firstValueAfterOperator=null;
		String secondValueAfterOperator=null;
		
		CSSExpressionMemberTermSimple a0=afterOperator.get(0);
		firstValueAfterOperator=a0.getValue();
		
		CSSExpressionMemberTermSimple a1=afterOperator.get(1);
		secondValueAfterOperator=a1.getValue();
		
		a0.setValue(secondValueAfterOperator);
		a1.setValue(firstValueAfterOperator);
			
	}
	
	
	private void propertyNameFlipper(CSSDeclaration dec){
		
		String propertyName=dec.getProperty();
		propertyName=propertyName.toLowerCase();		
		String nameToFlipTo=Mappings.PropertyName.get(propertyName);
		dec.setProperty(nameToFlipTo);
			
	}
	
	private void backgroundPositionFlipper(CSSDeclaration dec){
		
		CSSExpression expression=dec.getExpression();
		
		if(expression.getMemberCount()>0){		
			CSSExpressionMemberTermSimple member=(CSSExpressionMemberTermSimple)expression.getMemberAtIndex(0);
			String memberValue=member.getValue();
			if(memberValue.equalsIgnoreCase("right")){member.setValue("left");}
			else
			if(memberValue.equalsIgnoreCase("left")){member.setValue("right");}
			else{
				if(memberValue.endsWith("%")){
					// value with percentage
					memberValue=memberValue.replace("%", "");
					int percent=Integer.valueOf(memberValue);
					percent=100-percent;
					member.setValue(String.valueOf(percent)+"%");
				}
			}			
		}

			
	}
	
	
	private void backgroundFlipper(CSSDeclaration dec){
		
		CSSShortHandDescriptor paddingShorHand = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BACKGROUND);
		List <CSSDeclaration> splittedDecls = paddingShorHand.getSplitIntoPieces (dec);
		
		
		//add default values for missing fields
		List<ICSSExpressionMember> newMembersList =new ArrayList<ICSSExpressionMember>();
		for(CSSDeclaration splitted:splittedDecls){
			CSSExpression tempExpression=splitted.getExpression();
			
			for(int i=0;i<tempExpression.getMemberCount();i++){			
				newMembersList.add(tempExpression.getMemberAtIndex(i));
			}
			
		}

		dec.getExpression().removeAllMembers();
		for(ICSSExpressionMember tempMember : newMembersList){
			dec.getExpression().addMember(tempMember);
		}
		//End add default values for missing fields
		
		
		
		
		
		
		
		CSSDeclaration bacgroundPositionDec=null;
		for(CSSDeclaration splitted:splittedDecls){
			if(splitted.getProperty().equalsIgnoreCase("background-position")){
				bacgroundPositionDec=splitted;
				break;
			}
			
		}
		
		if(bacgroundPositionDec==null)return;
		
		
	
		
		CSSExpression expression=bacgroundPositionDec.getExpression();
		
		if(expression.getMemberCount()>0){		
			for(int i=0;i<expression.getMemberCount();i++){		
			
					CSSExpressionMemberTermSimple member=(CSSExpressionMemberTermSimple)expression.getMemberAtIndex(i);
					String memberValue=member.getValue();
					memberValue = memberValue.toLowerCase();
		
					if(memberValue.contains("right")){member.setValue(memberValue.replace("right", "left"));}
					else
					if(memberValue.contains("left")){member.setValue(memberValue.replace("left", "right"));}
					else{
						if(memberValue.endsWith("%")){
							// value with percentage
							memberValue=memberValue.replace("%", "");
							int percent=Integer.valueOf(memberValue);
							percent=100-percent;
							member.setValue(String.valueOf(percent)+"%");
						}
					}
			}
					
					
					
		}			
	}
	
	
	private void shadowBoxFlipper(CSSDeclaration dec){
		
		CSSExpression expression=dec.getExpression();
		if(expression.getMemberCount()>0){	
			CSSExpressionMemberTermSimple member=(CSSExpressionMemberTermSimple)expression.getMemberAtIndex(0);
			String memberValue=member.getValue();
			if(memberValue.startsWith("-")){member.setValue(memberValue.replace("-", ""));}else
			if(!memberValue.startsWith("-")){member.setValue("-" + memberValue);}
		}
			
	}
	

}
