import request from './request'

export function generateCampaign(data) {
  return request({ url: '/ai/campaign', method: 'post', data })
}
