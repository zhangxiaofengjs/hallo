import React from "react";
import "./style.less";

// 联系人类型定义
interface Contact {
  id: number;
  name: string;
  avatar: string;
  lastMessage: string;
  time: string;
  group: string;
}

interface ContactListProps {
  contacts: Contact[];
  selectedContact: number;
  onSelectContact: (id: number) => void;
}

// 分组状态类型
interface GroupState {
  [key: string]: boolean;
}

const ContactList: React.FC<ContactListProps> = ({
  contacts,
  selectedContact,
  onSelectContact,
}) => {
  // 分组状态管理，默认所有分组展开
  const [groupStates, setGroupStates] = React.useState<GroupState>(() => {
    const defaultStates: GroupState = {};
    const groups = new Set(contacts.map((c) => c.group));
    groups.forEach((group) => {
      defaultStates[group] = true;
    });
    return defaultStates;
  });

  // 按分组整理联系人
  const groupedContacts = contacts.reduce((acc, contact) => {
    const group = contact.group || "未分组";
    if (!acc[group]) {
      acc[group] = [];
    }
    acc[group].push(contact);
    return acc;
  }, {} as Record<string, Contact[]>);

  // 切换分组展开状态
  const toggleGroup = (groupName: string) => {
    setGroupStates((prev) => ({
      ...prev,
      [groupName]: !prev[groupName],
    }));
  };

  return (
    <div className="contacts-list">
      {Object.entries(groupedContacts).map(([groupName, groupContacts]) => (
        <div key={groupName} className="contact-group">
          <div className="group-header" onClick={() => toggleGroup(groupName)}>
            <span className="group-name">{groupName}</span>
            <span className="group-toggle">
              {groupStates[groupName] ? "▼" : "▶"}
            </span>
          </div>

          {groupStates[groupName] && (
            <div className="group-contacts">
              {groupContacts.map((contact) => (
                <div
                  key={contact.id}
                  className={`contact-item ${
                    selectedContact === contact.id ? "selected" : ""
                  }`}
                  onClick={() => onSelectContact(contact.id)}
                >
                  <div className="contact-avatar">{contact.avatar}</div>
                  <div className="contact-info">
                    <div className="contact-name">{contact.name}</div>
                    <div className="contact-last-message">
                      {contact.lastMessage}
                    </div>
                  </div>
                  <div className="contact-time">{contact.time}</div>
                </div>
              ))}
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default ContactList;
