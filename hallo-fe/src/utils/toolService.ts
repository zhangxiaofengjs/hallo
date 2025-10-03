import { v4 as uuidv4 } from 'uuid'

class ToolService {
  constructor() {}

  public getAvatarColor(uid: string): string {
    if (!uid || uid.length < 3) {
      return '#9370DB'
    }
    // uid的前三个字母的ASCII值生成颜色，注意要取模确保在0-255范围内
    const asciiSum = uid
      .substring(0, 3)
      .split('')
      .reduce((acc, char) => acc + char.charCodeAt(0), 0)
    const color = `rgb(${asciiSum % 256}, ${(asciiSum * 2) % 256}, ${(asciiSum * 3) % 256})`
    return color
  }

  public getUuid(): string {
    return uuidv4()
  }
}

export default new ToolService()
