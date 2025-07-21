import React, { useState } from "react";
import "./style.less";

interface SearchBarProps {
  onSearch: (searchTerm: string) => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchTerm(value);
    onSearch(value);
  };

  return (
    <div className="search-bar">
      <input
        type="text"
        placeholder="搜索联系人..."
        value={searchTerm}
        onChange={handleChange}
      />
      <span className="search-icon">🔍</span>
    </div>
  );
};

export default SearchBar;
