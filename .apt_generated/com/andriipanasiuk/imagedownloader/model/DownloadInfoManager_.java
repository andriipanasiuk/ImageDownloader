//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.andriipanasiuk.imagedownloader.model;

import android.content.Context;

public final class DownloadInfoManager_
    extends DownloadInfoManager
{

    private Context context_;

    private DownloadInfoManager_(Context context) {
        super(context);
        context_ = context;
        init_();
    }

    public static DownloadInfoManager_ getInstance_(Context context) {
        return new DownloadInfoManager_(context);
    }

    private void init_() {
    }

    public void rebind(Context context) {
        context_ = context;
        init_();
    }

}
