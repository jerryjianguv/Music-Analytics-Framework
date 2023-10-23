import React from 'react';
import { FrameworkState } from './framework';
import ReactECharts from 'echarts-for-react';

interface Props { }

class App extends React.Component<Props, FrameworkState> {
    private initialized: boolean = false;
    private input: (HTMLInputElement | null)[];
    
    constructor(props: Props) {
        super(props);
        this.state = {
            name: 'music playlist data analysis framework',
            footer: 'no plugin selected',
            dataPlugins: [],
            visualPlugins: [],
            graphJson: '',
            processDone: false,
        }
        this.input = [];
    }

    /**
     * start the application
     */
    async start() {
        const response = await fetch('/start');
        const json = await response.json();
        this.setState(json);
        this.input = new Array(this.state.dataPlugins.length);
    }

    /**
     * Create a button for selecting the visualization plugin
     * 
     * @param name Display type
     * @param index index of the display plugin.
     * @returns 
     */
    createVisualPlugin(name: string, index: number): React.ReactNode {
        return (
            <button onClick={this.chooseVisualPlugin(index)}>{name}</button>
        );
    }

    /**
     * Send link to client when user enters the text link
     * 
     * @param index The link of playlist that client wants to access
     * @returns An event handler for the click to send the link
     */
    sendLink(index: number): React.FormEventHandler {
        return async(e) => {
            e.preventDefault();
            const input = this.input[index];
            if (input != null) {
                const link = input.value;
                console.log('link is ' + link);
                const response = await fetch(`/getData?i=${index}&link=${link}`);
                const json = await response.json();
                this.setState(json);
            }
        }
    }

    /**
     * Create text box and submit buttons for each data plugin
     * 
     * @param name The name of the data plugin
     * @param index the index of the data plugin
     * @returns a text box with submit button
     */
    createTextboxes(name: string, index: number) : React.ReactNode{
        return (
            <form onSubmit={this.sendLink(index)}>
                <input
                    type="text"
                    placeholder="Put your link/path here"
                    ref={input => this.input[index] = input}/>
                <button type="submit">{name}</button>
            </form>
        )
    }

    chooseDataPlugin(index: number): React.MouseEventHandler {
        return async(e) => {
            e.preventDefault();
            const response = await fetch(`/getData?i=${index}`);
            const json = await response.json();
            this.setState(json);
        }
    }

    /**
     * Select a plugin to display processed data.
     * 
     * @param index index of the visualization plugin
     * @returns 
     */
    chooseVisualPlugin(index: number): React.MouseEventHandler {
        return async(e) => {
            e.preventDefault();
            const response = await fetch(`/display?i=${index}`);
            const json = await response.json();
            this.setState(json);
        }
    }

    componentDidMount(): void {
        if (!this.initialized) {
            this.start();
            this.initialized = true;
        }
    }

    /**
     * render the view according to the app state
     * @returns the total view of the app
     */
    render(): React.ReactNode {
        return (
            <div>
                <h2>Playlist Analyzer - fun visualizations for your favorite playlists</h2>
                <div id="datatextboxes">{this.state.dataPlugins.map((data_plugin, i) => this.createTextboxes(data_plugin.name, i))}</div>
                <div id="visualplugins">{this.state.visualPlugins.map((visual_plugin, i) => this.createVisualPlugin(visual_plugin.name, i))}</div>
                {this.state.graphJson !== '' && <ReactECharts option={JSON.parse(this.state.graphJson)} />}
            </div>
        )
    }
    
}

export default App;