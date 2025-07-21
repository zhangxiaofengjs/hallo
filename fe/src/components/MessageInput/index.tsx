import React, { useState, useRef, useEffect } from "react";
import "./style.less";

interface MessageInputProps {
  value: string;
  onChange: (value: string) => void;
  onSendMessage: (message: string) => void;
}

const MessageInput: React.FC<MessageInputProps> = ({
  value,
  onChange,
  onSendMessage,
}) => {
  const divRef = useRef<HTMLDivElement>(null);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (value.trim()) {
      onSendMessage(value);
      onChange("");
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLDivElement>) => {
    if (e.key === "Enter") {
      if (!e.shiftKey) {
        e.preventDefault();
        handleSubmit(e);
      } else {
        // 使用execCommand保持正确的换行行为
        document.execCommand("insertLineBreak");
        e.preventDefault();
      }
    }
  };

  const handleInput = (e: React.FormEvent<HTMLDivElement>) => {
    const newValue = e.currentTarget.textContent || "";
    onChange(newValue);
  };

  // 保持光标位置
  useEffect(() => {
    if (divRef.current) {
      if (value === "") {
        // 清空内容
        divRef.current.textContent = "";
      } else if (divRef.current.textContent !== value) {
        // 保存当前光标位置
        const selection = window.getSelection();
        let position = 0;

        if (selection && selection.rangeCount > 0) {
          const range = selection.getRangeAt(0);
          position = range.startOffset;
        }

        // 更新内容
        divRef.current.textContent = value;

        // 恢复光标位置
        if (selection) {
          const textNode = divRef.current.firstChild || divRef.current;
          const newPosition = Math.min(position, value.length);

          try {
            const range = document.createRange();
            range.setStart(textNode, newPosition);
            range.collapse(true);
            selection.removeAllRanges();
            selection.addRange(range);
          } catch (e) {
            console.error("Error setting cursor position:", e);
          }
        }
      }
    }
  }, [value]);

  return (
    <form className="input-area" onSubmit={handleSubmit}>
      <div
        ref={divRef}
        className="input-content"
        contentEditable
        onInput={handleInput}
        onKeyDown={handleKeyDown}
        data-placeholder="输入消息..."
      />
      <button type="submit">发送</button>
    </form>
  );
};

export default MessageInput;
