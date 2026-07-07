import request from './request'

export function createCampaign(data) {
  return request({ url: '/campaign/create', method: 'post', data })
}

export function listCampaigns(params) {
  return request({ url: '/campaign/list', method: 'get', params })
}

export function simpleCampaigns() {
  return request({ url: '/campaign/simple', method: 'get' })
}
