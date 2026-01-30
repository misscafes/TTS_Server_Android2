let cookie = ttsrv.userVars['cookie']


let req = {}
var callback = null
let ws = null // Websocket

function check() {
    cookie || function () { throw "未设置变量Cookie" }()
}

function id() {
    const num1 = Math.floor(1e8 + 9e8 * Math.random());
    const num2 = Math.floor(1e8 + 9e8 * Math.random());
    return String(num1) + String(num2);
}

var count = 0
var currentId = id()
function commonParams() {
    return `&mode=0&language=zh&browser_language=zh-CN&device_platform=web&aid=586861&real_aid=586861&pkg_type=release_version&device_id=${currentId}&tea_uuid=${currentId}&web_id=${currentId}&is_new_user=0&region=CN&sys_region=CN&use-olympus-account=1&samantha_web=1&version=1.20.1&version_code=20800&pc_version=1.20.1`
}

function checkAndRefreshId() {
    count++
    if (count >= 10) {
        count = 0
        currentId = id()
        logger.i("刷新设备ID: " + currentId)
        // 强制关闭WebSocket，下次请求会重建连接使用新ID
        if (ws != null) {
            ws.cancel()
            ws = null
        }
    }
}

let PluginJS = {
    "name": "豆包修复版",
    "id": "doubao.com",
    "author": "TTS Server",
    "version": 4,
    'iconUrl': `https://lf-flow-web-cdn.doubao.com/obj/flow-doubao/doubao/web/logo-icon.png`,
    'vars': {
        cookie: { label: "Cookie", hint: "完整的请求头Cookie", loginUrl: 'https://www.doubao.com/chat', binding: 'cookies', ua: "mobile" },
    },

    "onStop": function () {
        if (ws != null) {
            ws.cancel()
        }
    },


    "getAudioV2": function (request, callback2) {
        check()

        let rate = (request.rate * 2) - 100
        let pitch = request.pitch - 50
        // let volume = request.volume

        callback = callback2
        text = request.text
        speaker = request.voice

        req = {
            'text': text,
            'speaker': speaker,
            'rate': rate,
            'pitch': pitch,
        }

        // 检查是否需要刷新ID
        checkAndRefreshId()

        getAudio()
    },
}

function getAudio() {
    if (ws == null) {
        logger.i("init Websocket")
        let url = `wss://ws-samantha.doubao.com/samantha/audio/tts?format=aac&speaker=${req.speaker}&speech_rate=${req.rate}&pitch=${req.pitch}` + commonParams()
        let headers = {
            "Cookie": cookie,
            "Origin": "chrome-extension://capohkkfagimodmlpnahjoijgoocdjhd",
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36 Edg/133.0.0.0"
        }
        ws = new Websocket(url, headers)

        ws.on('close', function (code, reason) {
            ws = null
            if (code == 1000) {
                callback.close()
            } else {
                callback.error(reason)
            }
        })


        ws.on('error', function (err, resp) {
            ws = null
            console.error(resp.text())
            callback.error(err)
        })

        ws.on('binary', function (buffer) {
            // logger.d(buffer)
            callback.write(buffer)
        })

        ws.on('text', function (msg) {
            console.log(msg)
        })

        ws.on('open', function () {
            logger.d("open")
            getAudio()
        })

        return
    }

    if (ws.readyState = Websocket.OPEN) {
        sendMessage()
    } else {
        ws = null
        return getAudio()
    }
    function sendMessage() {
        logger.i("sendMessage....")
        send(`{"event":"text","podcast_extra":{"role":""},"text":"${req.text}"}`)
        send(`{"event":"finish"}`)
    }


    function send(msg) {
        let ok = ws.send(msg)
        if (!ok) {
            callback.error("send message failed: " + msg)
        }
    }
}


let locales = []
let voices = []
let EditorJS = {
    "getAudioSampleRate": function (locale, voice) {
        return 24000
    },

    "getLocales": function () {
        return locales
    },

    "getVoices": function (locale) {
        let mm = new Map()
        voices.forEach(v => {
            let tag = v.tag_list.map(t => t.tag_value).join("|")
            mm[v.style_id] = {
                name: v.name + " " + tag,
                iconUrl: v.icon.url
            }
        });


        return mm
    },

    // 加载本地或网络数据，运行在IO线程。
    "onLoadData": function () {
        check()
        let url = `https://www.doubao.com/alice/user_voice/recommend?language=zh&browser_language=zh-CN&mode=0&language=zh&browser_language=zh-CN&device_platform=web&aid=586861&real_aid=586861&pkg_type=release_version&device_id=${currentId}&tea_uuid=${currentId}&web_id=${currentId}&is_new_user=0&region=CN&sys_region=CN&use-olympus-account=1&samantha_web=1&version=1.20.1&version_code=20800&pc_version=1.20.1`
        let headers = {
            "Cookie": cookie,
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36 Edg/133.0.0.0"
        }

        function load(type, tab) {
            function verify(data) {
                if (data.code != 0) {
                    throw '获取失败(请尝试清缓存): ' + data.msg
                }
            }

            let filename = `voices_${type}_${tab}.json`
            let data= null 
            if (fs.exists(filename)) {
                data = JSON.parse(fs.readText(filename))
                verify(data)
            } else {
                let body = `{"page_index":1,"page_size":200,"recommend_type":${type},"tab_key": "${tab}"}`
                console.log(body)
                txt = http.post(url, body, headers).text()
                data = JSON.parse(txt)
                verify(data)
                fs.writeFile(filename, txt)
            }

            data.data.ugc_voice_list.forEach(v => {
                voices.push(v)
                console.log(voices[voices.length - 1].name)

                locales.includes(v.language_code) || locales.push(v.language_code)
            })
        }


        load(1, "")
        load(10, "female")
        load(10, "male")
        load(10, "characters")
        load(10, "accent")
    },

    "onLoadUI": function (ctx, linerLayout) {

    },

    "onVoiceChanged": function (locale, voiceCode) {

    }

}