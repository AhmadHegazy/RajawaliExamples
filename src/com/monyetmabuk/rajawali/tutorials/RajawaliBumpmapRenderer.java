package com.monyetmabuk.rajawali.tutorials;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.EllipticalOrbitAnimation3D;
import rajawali.animation.EllipticalOrbitAnimation3D.OrbitDirection;
import rajawali.lights.PointLight;
import rajawali.materials.NormalMapMaterial;
import rajawali.materials.NormalMapPhongMaterial;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.NormalMapTexture;
import rajawali.materials.textures.Texture;
import rajawali.math.Vector3;
import rajawali.math.Vector3.Axis;
import rajawali.parser.AParser.ParsingException;
import rajawali.parser.ObjParser;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;

public class RajawaliBumpmapRenderer extends RajawaliRenderer {
	private PointLight mLight;
	private BaseObject3D mHalfSphere1;
	private BaseObject3D mHalfSphere2;
	private Animation3D mLightAnim;

	public RajawaliBumpmapRenderer(Context context) {
		super(context);
		setFrameRate(60);
	}

	protected void initScene() {
		mLight = new PointLight();
		mLight.setPosition(-2, -2, 8);
		mLight.setPower(2f);
		getCurrentCamera().setPosition(0, 0, 6);

		ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.bumpsphere);
		try {
			objParser.parse();
			mHalfSphere1 = objParser.getParsedObject();
			mHalfSphere1.addLight(mLight);
			mHalfSphere1.setRotX(-90);
			mHalfSphere1.setY(-1.2f);
			addChild(mHalfSphere1);
			
			objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.bumptorus);
			objParser.parse();
			mHalfSphere2 = objParser.getParsedObject();
			mHalfSphere2.addLight(mLight);
			mHalfSphere2.setRotX(-45);
			mHalfSphere2.setY(1.2f);
			mHalfSphere2.setRotX(-45);
			addChild(mHalfSphere2);
	
			NormalMapMaterial material1 = new NormalMapMaterial();
			material1.addTexture(new Texture(R.drawable.sphere_texture));
			material1.addTexture(new NormalMapTexture(R.drawable.sphere_normal));
			mHalfSphere1.setMaterial(material1);
	
			NormalMapPhongMaterial material2 = new NormalMapPhongMaterial();
			material2.addTexture(new Texture(R.drawable.torus_texture));
			material2.addTexture(new NormalMapTexture(R.drawable.torus_normal));
			mHalfSphere2.setMaterial(material2);
		} catch(ParsingException e) {
			e.printStackTrace();
		} catch(TextureException tme) {
			tme.printStackTrace();
		}

		mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(0, 0, 4), new Vector3(0, 4, 0), Vector3.getAxisVector(Axis.Z), 0, 360, OrbitDirection.CLOCKWISE);
		mLightAnim.setDuration(5000);
		mLightAnim.setRepeatMode(RepeatMode.INFINITE);
		mLightAnim.setTransformable3D(mLight);
		registerAnimation(mLightAnim);
		mLightAnim.play();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		((RajawaliExampleActivity) mContext).showLoader();
		super.onSurfaceCreated(gl, config);
		((RajawaliExampleActivity) mContext).hideLoader();
	}
}
