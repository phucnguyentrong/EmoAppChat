package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */
public class MessageItem {
    public static final int TYPE_MYSELF = 0;
    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_FOOTER = 3;
    public static final int TYPE_MYSELF_STICKER = 4;
    public static final int TYPE_FRIEND_STICKER = 5;
    public static final int TYPE_DATE = 6;

    private int messageType;
    private String messageText;
    private String stickerUrl;
    private boolean isChecked = false;
    private boolean isWarning = false;
    private boolean mIsShowSentStatus = false;
    private boolean mIsShowDate = false;

    public boolean getVisibilityStatus(){
        return this.mIsShowSentStatus;
    }

    public void setVisibilityStatus(boolean status){
        this.mIsShowSentStatus = status;
    }

    public boolean getVisibilityDate(){
        return this.mIsShowDate;
    }

    public void setVisibilityDate(boolean status){
        this.mIsShowDate = status;
    }


    public boolean isWarning(){
        return isWarning;
    }

    public void setIsWarning(boolean isWarning){
        this.isWarning = isWarning;
    }
    public boolean isMyseft() {
        return messageType == TYPE_MYSELF || messageType == TYPE_MYSELF_STICKER;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }

    public static boolean isMyseft(int mViewType) {
        return mViewType == TYPE_MYSELF || mViewType == TYPE_MYSELF_STICKER;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}

