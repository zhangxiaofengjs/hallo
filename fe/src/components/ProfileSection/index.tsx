import React from "react";
import "./style.less";

interface ProfileSectionProps {
  username: string;
  status: string;
  avatar: string;
}

const ProfileSection: React.FC<ProfileSectionProps> = ({
  username,
  status,
  avatar,
}) => {
  return (
    <div className="profile-section">
      <div className="avatar">{avatar}</div>
      <div className="user-info">
        <div className="username">{username}</div>
        <div className="status">{status}</div>
      </div>
    </div>
  );
};

export default ProfileSection;
