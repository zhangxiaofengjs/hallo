import axios, {
  type AxiosInstance,
  type InternalAxiosRequestConfig,
  type AxiosResponse,
  AxiosError,
} from 'axios'
import errorService from './errorService'

class HttpService {
  private instance: AxiosInstance

  constructor() {
    // 创建axios实例
    this.instance = axios.create({
      baseURL: '/api', // 通过Vite代理访问后端API
      timeout: 120000,
    })

    // 设置请求拦截器
    this.setupRequestInterceptor()
    // 设置响应拦截器
    this.setupResponseInterceptor()
  }

  // 设置请求拦截器
  private setupRequestInterceptor(): void {
    this.instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // 添加认证token
        const token = localStorage.getItem('token')
        if (token) {
          config.headers = config.headers || {}
          config.headers.Authorization = `Bearer ${token}`
        }
        return config
      },
      (error: AxiosError) => {
        // 对请求错误做些什么
        return Promise.reject(error)
      }
    )
  }
  // 设置响应拦截器
  private setupResponseInterceptor(): void {
    this.instance.interceptors.response.use(
      (response: AxiosResponse) => {
        // 对响应数据做些什么，返回data而不是整个response
        return response
      },
      (error: AxiosError) => {
        // 对响应错误做些什么
        if (error.response) {
          // 服务器返回了错误状态码
          switch (error.response.status) {
            case 401:
              // 未授权，可能需要重新登录
              // 可以在这里处理登出逻辑
              break
            case 403:
              // 禁止访问
              console.error('访问被禁止')
              break
            case 404:
              // 请求的资源不存在
              console.error('请求的资源不存在')
              break
            case 500:
              // 服务器内部错误
              console.error('服务器内部错误')
              break
            default:
              console.error(`错误状态码: ${error.response.status}`)
          }
        } else if (error.request) {
          // 请求已发出但没有收到响应
          console.error('网络错误，请检查网络连接')
        } else {
          // 其他错误
          console.error('请求错误', error.message)
        }
        return Promise.reject(error)
      }
    )
  }

  // GET请求
  public get<T = any>(url: string, config?: InternalAxiosRequestConfig): Promise<T> {
    try {
      // 默认加上当前用户
      let userId = localStorage.getItem('userId')
      userId = '1'
      if (url.indexOf('?') === -1) {
        url = `${url}?userId=${userId}`
      } else {
        url = `${url}&userId=${userId}`
      }
      return this.instance
        .get<T>(url, config)
        .then((response) => response.data)
        .catch((error) => {
          // 系统错误
          return errorService.sysError(error)
        })
    } catch (error: any) {
      // 系统错误
      return errorService.sysError(error)
    }
  }

  // POST请求
  public post<T = any>(url: string, data?: any, config?: InternalAxiosRequestConfig): Promise<T> {
    return this.instance.post<T>(url, data, config).then((response) => response.data)
  }
}

const httpSevice = new HttpService()
export default httpSevice
