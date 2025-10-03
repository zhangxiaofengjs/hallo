import 'vue-router'
import type { UserType } from './user'

declare module 'vue-router' {
  interface RouteMeta {
    // 是可选的
  }

  interface RouteParams {
    type?: UserType
    uid?: string
  }
}
