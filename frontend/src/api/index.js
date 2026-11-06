import request from '../utils/request'

// 登录与个人信息
export const login = data => request.post('/auth/login', data)
export const getMe = () => request.get('/auth/me')

// 工单
export const getOrders = params => request.get('/order/page', { params })
export const getOrderDetail = id => request.get('/order/' + id)
export const createOrder = data => request.post('/order', data)
export const assignOrder = (id, workerId) =>
  request.put('/order/' + id + '/assign', null, { params: { workerId } })
export const finishOrder = (id, handleRemark) => request.put('/order/' + id + '/finish', { handleRemark })
export const evaluateOrder = (id, data) => request.put('/order/' + id + '/evaluate', data)

// 报修类型
export const getTypes = () => request.get('/type/list')
export const saveType = data => request.post('/type', data)
export const deleteType = id => request.delete('/type/' + id)

// 用户
export const getUsers = params => request.get('/user/page', { params })
export const addUser = data => request.post('/user', data)
export const changeUserStatus = (id, status) =>
  request.put('/user/' + id + '/status', null, { params: { status } })
export const resetUserPassword = id => request.put('/user/' + id + '/reset')
export const getWorkers = () => request.get('/user/workers')

// 公告
export const getNotices = () => request.get('/notice/list')
export const addNotice = data => request.post('/notice', data)
export const updateNotice = data => request.put('/notice', data)
export const deleteNotice = id => request.delete('/notice/' + id)

// 首页统计
export const getOverview = () => request.get('/stat/overview')
