package com.smartart.app;

import android.common.ResourceUtils;
import android.common.UIUtils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

/**
 * How to load a model from a .obj file
 *
 * @author dennis.ippel
 *
 */
public class ObjRender extends RendererActivity {
    private Object3dContainer objModel;

    @Override
    public void initScene() {

        scene.lights().add(new Light());

        IParser parser = null;

        try{
            String resID = ResourceUtils.getGlobalResourcePackageIdentifier(this.getBaseContext())+":raw/camaro_obj";
            Log.d("CHIRAG", resID);
            parser = Parser.createParser(Parser.Type.OBJ,
                    getResources(), resID, true);
        }catch(Exception ex){
            UIUtils.showSimpleErrorDialog(this, "Fatal Error", ex);
        }

        parser.parse();

        objModel = parser.getParsedObject();
        objModel.scale().x = objModel.scale().y = objModel.scale().z = .7f;
        scene.addChild(objModel);

    }

    @Override
    public void updateScene() {
        objModel.rotation().x++;
        objModel.rotation().z++;
    }
}
