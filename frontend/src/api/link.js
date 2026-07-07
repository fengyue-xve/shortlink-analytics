import request from './request'

export function createLink(data) {
  return request({ url: '/link/create', method: 'post', data })
}

export function listLinks(params) {
  return request({ url: '/link/list', method: 'get', params })
}

export function updateLink(data) {
  return request({ url: '/link/update', method: 'put', data })
}

export function deleteLink(id) {
  return request({ url: `/link/delete/${id}`, method: 'delete' })
}
