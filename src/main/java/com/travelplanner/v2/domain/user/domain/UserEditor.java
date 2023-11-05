package com.travelplanner.v2.domain.user.domain;

import lombok.Getter;

@Getter
public class UserEditor {
    private String userNickname;
    private String password; // 비밀번호 필드 추가
    private String profileImageUrl;

    public UserEditor(String userNickname, String password, String profileImageUrl) {
        this.userNickname = userNickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserEditorBuilder builder() {
        return new UserEditorBuilder();
    }

    public static class UserEditorBuilder {
        private String userNickname;
        private String password;
        private String profileImageUrl;

        UserEditorBuilder() {
        }

        public UserEditorBuilder userNickname(final String userNickname) {
            if (userNickname != null && !userNickname.isEmpty()) {
                this.userNickname = userNickname;
            }
            return this;
        }

        public UserEditorBuilder password(final String password) {
            if (password != null && !password.isEmpty()) {
                this.password = password;
            }
            return this;
        }

        public UserEditorBuilder profileImageUrl(final String profileImageUrl) {
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                this.profileImageUrl = profileImageUrl;
            }
            return this;
        }

        public UserEditor build() {
            return new UserEditor(userNickname, password, profileImageUrl);
        }
    }
}