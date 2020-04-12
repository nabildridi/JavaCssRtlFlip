package org.nd.css;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.decl.shorthand.CSSShortHandDescriptor;
import com.helger.css.decl.shorthand.CSSShortHandRegistry;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.property.ECSSProperty;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriterSettings;

public class DeclarationVisitor extends DefaultCSSVisitor {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	

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
			backgroundPositionFlipper( dec, "background-position");
		}
		
		//background position x
		if(propertyName.equals("background-position-x")){
			backgroundPositionFlipper( dec, "background-position-x");
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
		
		int membersCount=dec.getExpression().getMemberCount();
		if(membersCount<4)return;
		
		
		ICSSExpressionMember member1=dec.getExpression().getMemberAtIndex(1);
		ICSSExpressionMember member3=dec.getExpression().getMemberAtIndex(3);
		
		dec.getExpression().removeMember(1);
		dec.getExpression().addMember(1, member3);
		
		dec.getExpression().removeMember(3);
		dec.getExpression().addMember(3, member1);
			
	}
	

	private void radiusQuadFlipper(CSSDeclaration dec){
		
		int membersCount=dec.getExpression().getMemberCount();
		if(membersCount<4)return;
		
		
		ICSSExpressionMember member1=dec.getExpression().getMemberAtIndex(1);
		ICSSExpressionMember member3=dec.getExpression().getMemberAtIndex(3);
		
		dec.getExpression().removeMember(1);
		dec.getExpression().addMember(1, member3);
		
		dec.getExpression().removeMember(3);
		dec.getExpression().addMember(3, member1);
			
	}
	
	
	private void propertyNameFlipper(CSSDeclaration dec){
		
		String propertyName=dec.getProperty();
		propertyName=propertyName.toLowerCase();		
		String nameToFlipTo=Mappings.PropertyName.get(propertyName);
		dec.setProperty(nameToFlipTo);
			
	}
	
	private void backgroundPositionFlipper(CSSDeclaration dec, String decName){
		
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
								try {
									String withoutPercent = memberValue.replaceAll("%", "");
									int valueInt = Integer.parseInt(withoutPercent);
									valueInt = 100- valueInt;
									member.setValue(String.valueOf(valueInt) + "%");
								} catch (Exception e) {}
							}
						}	

		}

			
		
		
		
		
	}
	
	
	private void backgroundFlipper(CSSDeclaration dec){
		
		CSSShortHandDescriptor paddingShorHand = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BACKGROUND);
		List <CSSDeclaration> splittedDecls = paddingShorHand.getSplitIntoPieces (dec);

		
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
			
			CSSExpressionMemberTermSimple member=(CSSExpressionMemberTermSimple)expression.getMemberAtIndex(0);
			String memberValue=member.getValue();
			if(memberValue.endsWith("%")){
				// value with percentage
				try {
					String withoutPercent = memberValue.replaceAll("%", "");
					int valueInt = Integer.parseInt(withoutPercent);
					valueInt = 100- valueInt;
					member.setValue(String.valueOf(valueInt) + "%");
				} catch (Exception e) {}
			}
			
			
			CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
			String content = dec.getExpression().getAsCSSString(aSettings, 0);
			
			if(content.contains("right")){content = content.replace("right", "left");}
			else
			if(content.contains("left")){content = content.replace("left", "right");}	

			 CSSDeclarationList aList = CSSReaderDeclarationList.readFromString ( "background:" + content,   ECSSVersion.CSS30);
			 dec.setExpression(aList.getDeclarationAtIndex (0).getExpression());
			

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
