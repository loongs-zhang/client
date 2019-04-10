/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.letmefold.parser;

import android.text.TextUtils;
import android.util.Log;
import com.letmefold.exception.FaceErrorException;
import com.letmefold.model.FaceModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifyParser implements Parser<FaceModel> {
    @Override
    public FaceModel parse(String json) throws FaceErrorException {

        Log.d("VerifyParser", json);
        FaceModel faceModel = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int errorCode = jsonObject.optInt("error_code");
            if (errorCode > 0) {
                String errorMsg = jsonObject.optString("error_msg");
                FaceErrorException faceError = new FaceErrorException(errorCode, errorMsg);
                throw faceError;
            }
            JSONArray resultArray = jsonObject.optJSONArray("result");
            faceModel = new FaceModel();
            if (resultArray != null) {
                faceModel.setScore(resultArray.getDouble(0));
            }
            JSONObject extInfo = jsonObject.optJSONObject("ext_info");
            if (extInfo != null) {
                String faceliveness = extInfo.optString("faceliveness");
                if (!TextUtils.isEmpty(faceliveness)) {
                    faceModel.setFaceliveness(faceliveness);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return faceModel;
    }
}
