package elfEngine.basic.node.particle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import elfEngine.basic.debug.Debug;
import elfEngine.util.particleXml.XMLHelper;
import elfEngine.util.particleXml.XMLNode;
         
public class ParticlePlistHelper implements Constants {
	
	public float angleValue;
	public float angleVarianceValue;
	public int blendFuncDestinationValue;
	public int blendFuncSourceValue;
	public float durationValue;
	public float emitterTypeValue;
	public float finishColorAlphaValue;
	public float finishColorBlueValue;
	public float finishColorGreenValue;
	public float finishColorRedValue;
	public float finishColorVarianceAlphaValue;
	public float finishColorVarianceBlueValue;
	public float finishColorVarianceGreenValue;
	public float finishColorVarianceRedValue;
	public float finishParticleSizeValue;
	public float finishParticleSizeVarianceValue;
	public float gravityxValue;
	public float gravityyValue;
	public float maxParticlesValue;
	public float maxRadiusValue;
	public float maxRadiusVarianceValue;
	public float minRadiusValue;
	public float particleLifespanValue;
	public float particleLifespanVarianceValue;
	public float radialAccelVarianceValue;
	public float radialAccelerationValue;
	public float rotatePerSecondValue;
	public float rotatePerSecondVarianceValue;
	public float rotationEndValue;
	public float rotationEndVarianceValue;
	public float rotationStartValue;
	public float rotationStartVarianceValue;
	public float sourcePositionVariancexValue;
	public float sourcePositionVarianceyValue;
	public float sourcePositionxValue;
	public float sourcePositionyValue;
	public float speedValue;
	public float speedVarianceValue;
	public float startColorAlphaValue;
	public float startColorBlueValue;
	public float startColorGreenValue;
	public float startColorRedValue;
	public float startColorVarianceAlphaValue;
	public float startColorVarianceBlueValue;
	public float startColorVarianceGreenValue;
	public float startColorVarianceRedValue;
	public float startParticleSizeValue;
	public float startParticleSizeVarianceValue;
	public float tangentialAccelVarianceValue;
	public float tangentialAccelerationValue;
	public String textureFileNameValue;

	public ParticlePlistHelper(String plistPath) {
		if(plistPath ==null || !plistPath.endsWith(".plist")){
			System.err.println("error:"+plistPath);
			return ;
		}
		
		try {
			XMLNode root = XMLHelper.getRootFromFile(new File(plistPath));
			ArrayList<XMLNode> list = root.findByKey("key");
			
			for (XMLNode node : list) {
				String value = node.value;
				XMLNode next = root.findNextByNode(node);
				final String context = next.value;
				
				if (angle.equals(value)) {
					angleValue = Float.valueOf(context);
				} else if (angleVariance.equals(value)) {
					angleVarianceValue = Float.valueOf(context);
				} else if (blendFuncDestination.equals(value)) {
					
					blendFuncDestinationValue = Integer.valueOf(context);
				} else if (blendFuncSource.equals(value)) {
					 
					blendFuncSourceValue = Integer.valueOf(context);
				} else if (duration.equals(value)) {
					 
					durationValue = Float.valueOf(context);
				} else if (emitterType.equals(value)) {
					 
					emitterTypeValue = Float.valueOf(context);
				} else if (finishColorAlpha.equals(value)) {
					 
					finishColorAlphaValue = Float.valueOf(context);
				} else if (finishColorBlue.equals(value)) {
					 
					finishColorBlueValue = Float.valueOf(context);
				} else if (finishColorGreen.equals(value)) {
					 
					finishColorGreenValue = Float.valueOf(context);
				} else if (finishColorRed.equals(value)) {
					 
					finishColorRedValue = Float.valueOf(context);
				} else if (finishColorVarianceAlpha.equals(value)) {
					 
					finishColorVarianceAlphaValue = Float.valueOf(context);
				} else if (finishColorVarianceBlue.equals(value)) {
					 
					finishColorVarianceBlueValue = Float.valueOf(context);
				} else if (finishColorVarianceGreen.equals(value)) {
					 
					finishColorVarianceGreenValue = Float.valueOf(context);
				} else if (finishColorVarianceRed.equals(value)) {
					 
					finishColorVarianceRedValue = Float.valueOf(context);
				} else if (finishParticleSize.equals(value)) {
					 
					finishParticleSizeValue = Float.valueOf(context);
				} else if (finishParticleSizeVariance.equals(value)) {
					 
					finishParticleSizeVarianceValue = Float.valueOf(context);
				} else if (gravityx.equals(value)) {
					 
					gravityxValue = Float.valueOf(context);
				} else if (gravityy.equals(value)) {
					 
					gravityyValue = Float.valueOf(context);
				} else if (maxParticles.equals(value)) {
					 
					maxParticlesValue = Float.valueOf(context);
				} else if (maxRadius.equals(value)) {
					 
					maxRadiusValue = Float.valueOf(context);
				} else if (maxRadiusVariance.equals(value)) {
					 
					maxRadiusVarianceValue = Float.valueOf(context);
				} else if (minRadius.equals(value)) {
					 
					minRadiusValue = Float.valueOf(context);
				} else if (particleLifespan.equals(value)) {
					 
					particleLifespanValue = Float.valueOf(context);
				} else if (particleLifespanVariance.equals(value)) {
					 
					particleLifespanVarianceValue = Float.valueOf(context);
				} else if (radialAccelVariance.equals(value)) {
					 
					radialAccelVarianceValue = Float.valueOf(context);
				} else if (radialAcceleration.equals(value)) {
					 
					radialAccelerationValue = Float.valueOf(context);
				} else if (rotatePerSecond.equals(value)) {
					 
					rotatePerSecondValue = Float.valueOf(context);
				} else if (rotatePerSecondVariance.equals(value)) {
					 
					rotatePerSecondVarianceValue = Float.valueOf(context);
				} else if (rotationEnd.equals(value)) {
					 
					rotationEndValue = Float.valueOf(context);
				} else if (rotationEndVariance.equals(value)) {
					 
					rotationEndVarianceValue = Float.valueOf(context);
				} else if (rotationStart.equals(value)) {
					 
					rotationStartValue = Float.valueOf(context);
				} else if (rotationStartVariance.equals(value)) {
					 
					rotationStartVarianceValue = Float.valueOf(context);
				} else if (sourcePositionVariancex.equals(value)) {
					 
					sourcePositionVariancexValue = Float.valueOf(context);
				} else if (sourcePositionVariancey.equals(value)) {
					 
					sourcePositionVarianceyValue = Float.valueOf(context);
				} else if (sourcePositionx.equals(value)) {
					 
					sourcePositionxValue = Float.valueOf(context);
				} else if (sourcePositiony.equals(value)) {
					 
					sourcePositionyValue = Float.valueOf(context);
				} else if (speed.equals(value)) {
					 
					speedValue = Float.valueOf(context);
				} else if (speedVariance.equals(value)) {
					 
					speedVarianceValue = Float.valueOf(context);
				} else if (startColorAlpha.equals(value)) {
					 
					startColorAlphaValue = Float.valueOf(context);
				} else if (startColorBlue.equals(value)) {
					 
					startColorBlueValue = Float.valueOf(context);
				} else if (startColorGreen.equals(value)) {
					 
					startColorGreenValue = Float.valueOf(context);
				} else if (startColorRed.equals(value)) {
					 
					startColorRedValue = Float.valueOf(context);
				} else if (startColorVarianceAlpha.equals(value)) {
					 
					startColorVarianceAlphaValue = Float.valueOf(context);
				} else if (startColorVarianceBlue.equals(value)) {
					 
					startColorVarianceBlueValue = Float.valueOf(context);
				} else if (startColorVarianceGreen.equals(value)) {
					 
					startColorVarianceGreenValue = Float.valueOf(context);
				} else if (startColorVarianceRed.equals(value)) {
					 
					startColorVarianceRedValue = Float.valueOf(context);
				} else if (startParticleSize.equals(value)) {
					 
					startParticleSizeValue = Float.valueOf(context);
				} else if (startParticleSizeVariance.equals(value)) {
					 
					startParticleSizeVarianceValue = Float.valueOf(context);
				} else if (tangentialAccelVariance.equals(value)) {
					 
					tangentialAccelVarianceValue = Float.valueOf(context);
				} else if (tangentialAcceleration.equals(value)) {
					 
					tangentialAccelerationValue = Float.valueOf(context);
				} else if (textureFileName.equals(value)) {
					 
					textureFileNameValue = context;
				}  else {
					Debug.e("ParticlePlistHelper : no found "+value);
				}
			}
		} catch (SAXException sax) {
			sax.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
	}
}
